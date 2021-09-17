package com.decard.mqttlibs

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.apache.activemq.ActiveMQConnectionFactory
import javax.jms.*


class ActiveMQActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_mqactivity)
    }

    @Throws(Exception::class)
    fun testActiveMQConsumer() {
        //创建连接工厂对象
        val connectionFactory: ConnectionFactory =
            ActiveMQConnectionFactory("tcp://123.56.4.237:1884")
        //创建连接
        val createConnection: Connection = connectionFactory.createConnection()
        //开启连接
        createConnection.start()
        //创建session
        val createSession: Session = createConnection.createSession(false, Session.AUTO_ACKNOWLEDGE)
        //创建一个Destination对象
        val createQueue: Queue = createSession.createQueue("update_decard_hbs_queue_00010900202103900003")
        //创建消费者
        val createConsumer = createSession.createConsumer(createQueue)
        //接受消息
        createConsumer.messageListener = MessageListener { message ->
            val textMessage = message as TextMessage
            val text: String
            try {
                text = textMessage.text
                println(text)
            } catch (e: JMSException) {
            }
        }
        //等待接受消息
        System.`in`.read()
        //关闭资源
        createConsumer.close()
        createSession.close()
        createConnection.close()
    }
}