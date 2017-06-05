package io.renren.controller.jms;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import io.renren.controller.AbstractController;
import io.renren.utils.R;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhaoxijun
 * @date 2017年5月23日
 */
@RestController
@RequestMapping("/jms/activeMq")
public class ActiveMqController extends AbstractController {
	
	@Resource(name="jmsTemplate")
    private JmsTemplate jmsTemplate;
	
	//队列名gzframe.demo
    @Resource(name="demoQueueDestination")
    private Destination demoQueueDestination;
	
	@RequestMapping("/set")
	@ResponseBody
	public R set(final String msg){
		System.out.println("向队列" + demoQueueDestination.toString() + "发送了消息------------" + msg);
        jmsTemplate.send(demoQueueDestination, new MessageCreator() {
          public Message createMessage(Session session) throws JMSException {
            return session.createTextMessage(msg);
          }
        });
		return R.ok("success");
	}
	
	
	@RequestMapping("/get")
	@ResponseBody
	public R get(){
		TextMessage tm = (TextMessage) jmsTemplate.receive(demoQueueDestination);
        try {
            System.out.println("从队列收到了消息：\t"
                    + tm.getText());
            return R.ok("success").put("msgObj", tm).put("msg", tm.getText());
        } catch (JMSException e) {
            e.printStackTrace();
            return R.error();
        }
        
		
	}
}
