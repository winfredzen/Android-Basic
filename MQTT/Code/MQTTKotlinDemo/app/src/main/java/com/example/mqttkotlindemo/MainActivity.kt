package com.example.mqttkotlindemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity() {

    private lateinit var mqttClient: MqttAndroidClient

    companion object {
        const val TAG = "AndroidMqttClient"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //连接 MQTT 服务器
    fun connect(context: Context) {
        val serverURI = "tcp://broker.emqx.io:1883"
        mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        mqttClient.setCallback(object : MqttCallback{
            override fun connectionLost(cause: Throwable?) {//与 broker 连接丢失
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) { //收到 broker 新消息
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                Toast.makeText(this@MainActivity, topic + ":" + message.toString(), Toast.LENGTH_SHORT).show();
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) { //消息到 broker 传递完成
                Log.d(TAG, "deliveryComplete")
            }
        })

        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object: IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }

    //创建 MQTT 订阅
    //订阅 topic

    fun subscribe(topic: String, qos: Int = 1) {

        try {
            mqttClient.subscribe(topic, qos, null, object: IMqttActionListener{
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Subscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to subscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    //取消订阅
    //取消订阅 topic
    fun unsubscribe(topic: String) {
        try {
            mqttClient.unsubscribe(topic, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Unsubscribed to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to unsubscribe $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    //发布消息
    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })

        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    //断开 MQTT 连接
    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun onClick(view: android.view.View) {
        when (view.id) {
            R.id.connect_btn -> connect(this.applicationContext)
            R.id.subscribe_btn -> subscribe("wzTopic")
            R.id.unsubscribe_btn -> unsubscribe("wzTopic")
            R.id.publish_btn -> publish("wzTopic", "wzTest001")
            R.id.disconnect_btn -> disconnect()
            else -> print("no case")
        }
    }

}






















