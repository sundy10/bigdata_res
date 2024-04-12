#Flink读写Kafka
##kafka -> kafka
```
CREATE TABLE test (
  uid BIGINT,
  name STRING
) WITH (
 'connector' = 'kafka',
 'topic' = 'test',
 'properties.bootstrap.servers' = '127.0.0.1:9092',
 'properties.group.id' = 'testGroup',
 'format' = 'json',
 'scan.startup.mode' = 'earliest-offset',
 'json.fail-on-missing-field' = 'false',
 'json.ignore-parse-errors' = 'true'
);

CREATE TABLE test_kafka (
  uid BIGINT,
  name STRING
) WITH (
 'connector' = 'kafka',
 'topic' = 'test_kafka',
 'properties.bootstrap.servers' = '127.0.0.1:9092',
 'properties.group.id' = 'testGroup',
 'format' = 'json',
 'scan.startup.mode' = 'earliest-offset',
 'json.fail-on-missing-field' = 'false',
 'json.ignore-parse-errors' = 'true'
);

insert into test_kafka select uid,name from test;

```

##kafka -> mysql
###指定消费组进行消费

```
CREATE TABLE src_kafka (
  uid BIGINT,
  name STRING,
  content STRING,
  `offset` BIGINT METADATA VIRTUAL,  -- from Kafka connector
  `rowtime` TIMESTAMP(3) METADATA FROM 'timestamp', 
  `proctime` AS PROCTIME()
) WITH (
 'connector' = 'kafka',
 'topic' = 'test',
 'properties.bootstrap.servers' = '127.0.0.1:9092',
 'properties.group.id' = 'testGroup',
 'format' = 'json',
 'scan.startup.mode' = 'group-offsets',--默认值
 'properties.auto.offset.reset' = 'earliest', --防止第一次读取时没有offset报错
 'json.fail-on-missing-field' = 'false',
 'json.ignore-parse-errors' = 'true'
);

CREATE TABLE kafka_sink (
    uid BIGINT,
    name STRING,
    content STRING,
    `offset` BIGINT,
    `rowtime` TIMESTAMP(3),
    `proctime` TIMESTAMP(3)
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://127.0.0.1:3306/flink?useUnicode=true&characterEncoding=UTF-8',
   'username' = 'root',
   'password' = '11111111',
   'table-name' = 'kafka_sink',
   'driver' = 'com.mysql.cj.jdbc.Driver'
);

insert into kafka_sink select * from src_kafka;
```

##Kafka -> TiDB

```
-- checkpoint间隔时间，单位毫秒，没有默认值，如果想开启checkpoint，需要将该参数设置一个大于0的数值
-- 如果想提升sink性能，比如写hudi，需要将该值设置大一点，因为间隔时间决定了批次大小
-- checkpoint间隔时间不能设置太短也不能设置太长，太短影响写入性能，太长影响数据及时性。
set execution.checkpointing.interval=10000; --10s
-- 保存checkpoint文件的目录
set state.checkpoints.dir=file:///Users/feicheng/bigdata/data/flink-tmp/checkpoint;
-- 任务取消后保留checkpoint,默认值NO_EXTERNALIZED_CHECKPOINTS，
-- 可选值NO_EXTERNALIZED_CHECKPOINTS、DELETE_ON_CANCELLATION、RETAIN_ON_CANCELLATION
set execution.checkpointing.externalized-checkpoint-retention=RETAIN_ON_CANCELLATION;

--  set execution.savepoint.path=file:///Users/feicheng/bigdata/data/flink-tmp/checkpoint/d82157eb8bdffb984189389e0d9c0716;



CREATE TABLE src_kafka (
  uid BIGINT,
  name STRING,
  content STRING,
  `offset` BIGINT METADATA VIRTUAL,  -- from Kafka connector
  `rowtime` TIMESTAMP(3) METADATA FROM 'timestamp', 
  `proctime` AS PROCTIME()
) WITH (
 'connector' = 'kafka',
 'topic' = 'test',
 'properties.bootstrap.servers' = '127.0.0.1:9092',
 'properties.group.id' = 'testGroup1',
 'format' = 'json',
 'scan.startup.mode' = 'group-offsets',
 'json.fail-on-missing-field' = 'false',
 'json.ignore-parse-errors' = 'true'
);

CREATE TABLE kafka_sink (
    uid BIGINT,
    name STRING,
    content STRING,
    `offset` BIGINT,
    `rowtime` TIMESTAMP(3),
    `proctime` TIMESTAMP(3)
) WITH (
   'connector' = 'jdbc',
   'url' = 'jdbc:mysql://127.0.0.1:4000/flink?useUnicode=true&characterEncoding=UTF-8',
   'username' = 'root',
   'password' = '',
   'table-name' = 'kafka_sink',
   'driver' = 'com.mysql.cj.jdbc.Driver'
);

insert into kafka_sink select * from src_kafka;
```