package com.yuanqing.project.tiansu.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(value = 30)
public class RabbitMQConfig {
    protected static Logger LOGGER = LoggerFactory.getLogger(RabbitMQConfig.class);

    //登录日志
    @Bean
    public Queue loginLogQueue() {
        LOGGER.error("----------- init queue:{}", IConstants.RABBITMQ_QUEUE_LOGINLOG);
        return new Queue(IConstants.RABBITMQ_QUEUE_LOGINLOG);
    }
//    //自审日志
//    @Bean
//    public Queue selfAuditQueue() {
//        LOGGER.error("----------- init queue:{}", IConstants.RABBITMQ_QUEUE_SELFAUDIT);
//        return new Queue(IConstants.RABBITMQ_QUEUE_SELFAUDIT);
//    }
    //系统日志
    @Bean
    public Queue logQueue() {
        LOGGER.error("----------- init queue:{}", IConstants.RABBITMQ_QUEUE_LOG);
        return new Queue(IConstants.RABBITMQ_QUEUE_LOG);
    }


    /**
     * 交换机
     * @return
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(IConstants.RABBITMQ_EXCHANGE);
    }

//    /**
//     * 自审信息队列绑定到交换机
//     * @param selfAuditQueue
//     * @param exchange
//     * @return
//     */
//    @Bean
//    public Binding bindingExchangeAuditLog(Queue selfAuditQueue, TopicExchange exchange) {
//        return BindingBuilder.bind(selfAuditQueue).to(exchange).with(IConstants.RABBITMQ_QUEUE_SELFAUDIT);
//    }

    /**
     * 系统日志绑定到交换机
     * @param logQueue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingExchangeLog(Queue logQueue, TopicExchange exchange) {
        return BindingBuilder.bind(logQueue).to(exchange).with(IConstants.RABBITMQ_QUEUE_LOG);
    }

    /**
     * 系统日志绑定到交换机
     * @param loginLogQueue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingExchangeLoginLog(Queue loginLogQueue, TopicExchange exchange) {
        return BindingBuilder.bind(loginLogQueue).to(exchange).with(IConstants.RABBITMQ_QUEUE_LOGINLOG);
    }

}
