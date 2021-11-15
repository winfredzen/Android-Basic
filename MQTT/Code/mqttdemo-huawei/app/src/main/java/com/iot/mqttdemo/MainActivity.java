package com.iot.mqttdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String TAG = "MqttDemo";
    //private final static String IOT_PLATFORM_URL = "iot-mqtts.cn-north-4.myhuaweicloud.com";
    private final static String IOT_PLATFORM_URL = "broker-cn.emqx.io";
    private Context mContext;
    private Toast mToast;
    private long minBackoff = 1000;
    private long maxBackoff = 30 * 1000; //30 seconds
    private long defaultBackoff = 1000;
    private static int retryTimes = 0;
    private SecureRandom random = new SecureRandom();

    private EditText editText_mqtt_device_connect_deviceId;
    private EditText editText_mqtt_device_connect_password;
    private EditText editText_mqtt_gateway_data_report_service_id;
    private EditText editText_mqtt_gateway_data_report_property_key;
    private EditText editText_mqtt_gateway_data_report_property_value;
    private EditText editText_mqtt_log;

    private CheckBox checkbox_mqtt_connet_ssl;
    private Spinner spinner_mqtt_connect_qos;
    private ArrayAdapter<Integer> arr_adapter_qos_list;

    private MqttAndroidClient mqttAndroidClient;
    int qos = 0;
    boolean isSSL = false;
    boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);

        initView();
        initQosList();
    }

    private void showToast(String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void initView() {
        editText_mqtt_device_connect_deviceId = findViewById(R.id.editText_mqtt_device_connect_deviceId);
        editText_mqtt_device_connect_password = findViewById(R.id.editText_mqtt_device_connect_password);
        editText_mqtt_gateway_data_report_service_id = findViewById(R.id.editText_mqtt_gateway_data_report_service_id);
        editText_mqtt_gateway_data_report_property_key = findViewById(R.id.editText_mqtt_gateway_data_report_property_key);
        editText_mqtt_gateway_data_report_property_value = findViewById(R.id.editText_mqtt_gateway_data_report_property_value);
        editText_mqtt_log = findViewById(R.id.editText_mqtt_log);
        findViewById(R.id.button_mqtt_device_connect).setOnClickListener(this);
        findViewById(R.id.button_mqtt_property_data_report).setOnClickListener(this);
        findViewById(R.id.textView_mqtt_log).setOnClickListener(this);

        spinner_mqtt_connect_qos = findViewById(R.id.spinner_mqtt_connect_qos);
        checkbox_mqtt_connet_ssl = findViewById(R.id.checkbox_mqtt_connet_ssl);

        checkbox_mqtt_connet_ssl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSSL = true;
                    checkbox_mqtt_connet_ssl.setText("SSL加密");
                } else {
                    isSSL = false;
                    checkbox_mqtt_connet_ssl.setText("SSL不加密");
                }
            }
        });
    }

    private void initQosList() {
        List<Integer> device_list = new ArrayList<>();
        device_list.add(0);
        device_list.add(1);
        arr_adapter_qos_list = new ArrayAdapter<Integer>(mContext, android.R.layout.simple_spinner_item, device_list);
        arr_adapter_qos_list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_mqtt_connect_qos.setAdapter(arr_adapter_qos_list);

        spinner_mqtt_connect_qos.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                qos = (int) spinner_mqtt_connect_qos.getSelectedItem();
                Log.d(TAG, "select qos:" + qos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_mqtt_device_connect:
//                if (TextUtils.isEmpty(editText_mqtt_device_connect_deviceId.getText())
//                        || TextUtils.isEmpty(editText_mqtt_device_connect_password.getText())) {
//                    showToast("请填写设备ID、设备密钥！");
//                    return;
//                }
                initMqttConnects();
                break;
            case R.id.button_mqtt_property_data_report:
                if (TextUtils.isEmpty(editText_mqtt_gateway_data_report_service_id.getText())
                        || TextUtils.isEmpty(editText_mqtt_gateway_data_report_property_key.getText())
                        || TextUtils.isEmpty(editText_mqtt_gateway_data_report_property_value.getText())) {
                    showToast("请填写服务ID、属性、值！");
                    return;
                }
                if (!isConnect) {
                    showToast("请先建立连接");
                    return;
                }
                reportProperties();
                break;
            case R.id.textView_mqtt_log:
                editText_mqtt_log.setText("");
                break;
        }
    }

    /**
     * 建立MQTT/MQTTS连接
     */
    private void initMqttConnects() {
        Log.i(TAG, "Start mqtt connect, isSSL:" + isSSL);
        String serverUrl;
        if (isSSL) {
            editText_mqtt_log.append("开始建立MQTTS连接" + "\n");
            serverUrl = "ssl://" + IOT_PLATFORM_URL + ":8883";
        } else {
            editText_mqtt_log.append("开始建立MQTT连接" + "\n");
            serverUrl = "tcp://" + IOT_PLATFORM_URL + ":1883";
        }

        String currentDate = ConnectUtils.getCurrentDate();
        String clientId = editText_mqtt_device_connect_deviceId.getText().toString() + "_0_0_" + currentDate;
        Log.d(TAG, "serverUrl:" + serverUrl + ", clientId:" + clientId);
        editText_mqtt_log.append("serverUrl:" + serverUrl + ", clientId:" + clientId + "\n");
        MqttConnectOptions mqttConnectOptions = intitMqttConnectOptions(currentDate);

        mqttAndroidClient = new MqttAndroidClient(mContext, serverUrl, clientId);
        mqttAndroidClient.setCallback(new MqttCallBack4IoTHub());

        try {
            if (isSSL) {
                mqttConnectOptions.setSocketFactory(ConnectUtils.getMqttsCerificate(mContext));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    exception.printStackTrace();
                    Log.e(TAG, "Fail to connect to: " + exception.getMessage());
                    editText_mqtt_log.append("建立连接失败:" + exception.getMessage() + "\n");

                    //退避重连
                    int lowBound = (int) (defaultBackoff * 0.8);
                    int highBound = (int) (defaultBackoff * 1.2);
                    long randomBackOff = random.nextInt(highBound - lowBound);
                    long backOffWithJitter = (int) (Math.pow(2.0, (double) retryTimes)) * (randomBackOff + lowBound);
                    long waitTImeUntilNextRetry = (int) (minBackoff + backOffWithJitter) > maxBackoff ? maxBackoff : (minBackoff + backOffWithJitter);
                    try {
                        Thread.sleep(waitTImeUntilNextRetry);
                    } catch (InterruptedException e) {
                        System.out.println("sleep failed, the reason is" + e.getMessage().toString());
                    }
                    retryTimes++;
                    MainActivity.this.initMqttConnects();
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, e.getMessage());
            editText_mqtt_log.append("建立连接异常:" + e.getMessage() + "\n");
        }
    }

    private MqttConnectOptions intitMqttConnectOptions(String currentDate) {
        String password = ConnectUtils.sha256_HMAC(editText_mqtt_device_connect_password.getText().toString(), currentDate);
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setKeepAliveInterval(120);
        mqttConnectOptions.setConnectionTimeout(30);
        //mqttConnectOptions.setUserName(editText_mqtt_device_connect_deviceId.getText().toString());
        //mqttConnectOptions.setPassword(password.toCharArray());
        return mqttConnectOptions;
    }

    /**
     * Mqtt 消息回调函数
     */
    private final class MqttCallBack4IoTHub implements MqttCallbackExtended {

        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "The connection Lost.");
            editText_mqtt_log.append("连接失败" + "\n");
            isConnect = false;
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "Incoming message: " + new String(message.getPayload(),
                    StandardCharsets.UTF_8));
            editText_mqtt_log.append("MQTT接收下发命令成功：" + message + "\n");
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.i(TAG, "The message send success!");
            editText_mqtt_log.append("MQTT属性上报成功" + "\n");
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            if (reconnect) {
                Log.i(TAG, "Reconnected to :" + serverURI);
                editText_mqtt_log.append("重连后重新订阅" + "\n");
                subscribeToTopic();
            }
            isConnect = true;
            Log.i(TAG, "The client connected to IoT-MQTT-Server " + serverURI + " successful.");
            editText_mqtt_log.append("MQTT连接成功：" + serverURI + "\n");
            showToast("MQTT连接成功");
        }
    }

    /**
     * 订阅命令下发topic
     *
     * @return
     */
    private String getSubscriptionTopic() {
        String mqtt_sub_topic_command_json = String.format("$oc/devices/%s/sys/commands/#", editText_mqtt_device_connect_deviceId.getText().toString());
        return mqtt_sub_topic_command_json;
    }

    /**
     * 发布属性上报topic
     *
     * @return
     */
    private String getPublishTopic() {
        String mqtt_report_topic_json = String.format("$oc/devices/%s/sys/properties/report", editText_mqtt_device_connect_deviceId.getText().toString());
        return mqtt_report_topic_json;
    }

    /**
     * 属性上报
     */
    private void reportProperties() {
        String message = setMessage();
        if (TextUtils.isEmpty(message)) {
            showToast("属性内容有误！");
            return;
        }
        Log.i(TAG, "Report properties content:" + message);
        editText_mqtt_log.append("属性上报内容:" + message + "\n");
        publishMessage(message, getPublishTopic());
    }

    private String setMessage() {
        String result = "";
        JSONArray array = new JSONArray();
        JSONObject services = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject message = new JSONObject();
        try {
            properties.put(editText_mqtt_gateway_data_report_property_key.getText().toString(), editText_mqtt_gateway_data_report_property_value.getText().toString());
            services.put("service_id", editText_mqtt_gateway_data_report_service_id.getText().toString());
            services.put("properties", properties);
            array.put(services);
            message.put("services", array);
            result = message.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * mqtt推送消息
     *
     * @param publishMessage
     * @param publishTopic
     */
    private void publishMessage(String publishMessage, String publishTopic) {
        try {
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload(publishMessage.getBytes());
            mqttAndroidClient.publish(publishTopic, mqttMessage);
            Log.i(TAG, "Message Published");
            editText_mqtt_log.append("属性上报topic:" + publishTopic + "\n");
            editText_mqtt_log.append("MQTT推送消息：" + mqttMessage + "\n");
            if (!mqttAndroidClient.isConnected()) {
                Log.i(TAG, mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error Publishing: " + e.getMessage());
            editText_mqtt_log.append("MQTT发送消息失败：" + e.getMessage() + "\n");
        }
    }

    /**
     * Mqtt到iot平台订阅topic
     */
    private void subscribeToTopic() {
        Log.i(TAG, "begin to subscribe topics");
        editText_mqtt_log.append("开始订阅命令下发的Topic：" + getSubscriptionTopic() + "\n");
        try {
            mqttAndroidClient.subscribe(getSubscriptionTopic(), qos, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "Topic subscribe success!");
                    editText_mqtt_log.append("订阅Topic成功" + "\n");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to subscribe.");
                    editText_mqtt_log.append("订阅Topic失败：" + exception.getMessage() + "\n");
                }
            });
        } catch (MqttException ex) {
            Log.e(TAG, "Exception whilst subscribing" + ex.getMessage());
            editText_mqtt_log.append("订阅Topic异常：" + ex.getMessage() + "\n");
        }
    }

    /**
     * 取消订阅
     */
    private void unsubscribe() {
        Log.i(TAG, "begin to unsubscribe topics");
        editText_mqtt_log.append("取消订阅命令下发Topic：" + getSubscriptionTopic() + "\n");
        try {
            mqttAndroidClient.unsubscribe(getSubscriptionTopic(), null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.i(TAG, "Topic unsubscribe success!");
                    editText_mqtt_log.append("取消订阅成功。" + "\n");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.e(TAG, "Failed to unsubscribe!");
                    editText_mqtt_log.append("取消订阅失败。" + "\n");
                }
            });
        } catch (MqttException e) {
            Log.e(TAG, "Exception whilst subscribing" + e.getMessage());
            editText_mqtt_log.append("取消订阅Topic异常：" + e.getMessage() + "\n");
        }
    }

    private void disconnected() {
        try {
            mqttAndroidClient.disconnect();
            editText_mqtt_log.append("断开连接" + "\n");
            isConnect = false;
        } catch (MqttException e) {
            Log.e(TAG, "disconnected failed.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mqttAndroidClient != null) {
            unsubscribe();
            disconnected();
        }
    }
}
