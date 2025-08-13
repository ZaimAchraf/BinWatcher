# 📊 Chapter 08: Logging with ELK (Elasticsearch, Logstash, Kibana)

## 🌟 **Chapter Summary**
This chapter introduces centralized logging using the **ELK stack**.  
Logs from all microservices are sent to **Logstash**, stored in **Elasticsearch**, and visualized in **Kibana**.  
This setup improves monitoring, debugging, and observability across the microservices architecture.

---

## 🛠️ **Dependencies**
Add Logstash Logback encoder to your microservices:

```xml
<dependency>
    <groupId>net.logstash.logback</groupId>
    <artifactId>logstash-logback-encoder</artifactId>
    <version>7.4</version>
</dependency>
```

## 📝 Logback Configuration
- Logs are sent to console (for development) and Logstash (for centralized collection).  
- Different profiles for dev and prod environments.  
- Example logback-spring.xml snippet:

```xml
<configuration>
    <property name="LOG_PATH" value="./logs"/>
    <property name="LOG_LEVEL" value="INFO"/>
    <property name="springProfile" value="${spring.profiles.active:-dev}" />

    <appender name="LOGSTASH" class="net.logstash.logback.appender.LogstashTcpSocketAppender">
        <destination>localhost:5044</destination>
        <encoder class="net.logstash.logback.encoder.LogstashEncoder">
            <customFields>{"service": "gateway-service", "env": "${springProfile}"}</customFields>
        </encoder>
    </appender>

    <springProfile name="dev">
        <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
            <encoder>
                <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
            </encoder>
        </appender>
        <root level="${LOG_LEVEL}">
            <appender-ref ref="CONSOLE"/>
            <appender-ref ref="LOGSTASH"/>
        </root>
    </springProfile>

    <springProfile name="prod">
        <root level="${LOG_LEVEL}">
            <appender-ref ref="LOGSTASH"/>
        </root>
    </springProfile>
</configuration>
```

## 🔧 Logstash Pipeline Configuration
- Input listens on TCP 5044.  
- Filters can enrich logs with environment data.  
- Output sends logs to Elasticsearch, indexed by service and date.

```conf
input {
    tcp {
        port => 5044
        codec => json_lines
    }
}

filter {
  mutate {
    add_field => { "env" => "%{env}" }
  }
}

output {
    elasticsearch {
        hosts => ["http://elasticsearch:9200"]
        index => "%{service}-%{+YYYY.MM.dd}"
    }
}
```

## 🐳 Docker Compose for ELK
- Elasticsearch, Logstash, and Kibana are all launched in Docker containers.  
- Network elk used for communication.

```yaml
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:7.17.13
  container_name: elasticsearch
  environment:
    - discovery.type=single-node
    - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
  ports:
    - "9200:9200"
  networks:
    - elk

logstash:
  image: docker.elastic.co/logstash/logstash:7.17.13
  container_name: logstash
  volumes:
    - ./logstash.conf:/usr/share/logstash/pipeline/logstash.conf
  ports:
    - "5044:5044"
    - "5000:5000/tcp"
    - "5000:5000/udp"
    - "9600:9600"
  depends_on:
    - elasticsearch
  networks:
    - elk

kibana:
  image: docker.elastic.co/kibana/kibana:7.17.13
  container_name: kibana
  environment:
    - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
  ports:
    - "5601:5601"
  depends_on:
    - elasticsearch
  networks:
    - elk

networks:
  elk:
    driver: bridge
```

## ✅ Benefits
- Centralized logging across all services.  
- Easy monitoring and debugging.  
- Time-based and service-based indexing for fast searches.  
- Supports multi-environment setups (dev/prod) with custom fields.

---
