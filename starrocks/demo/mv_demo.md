CREATE TABLE goods1(
    item_id1          INT NOT NULL,
    item_name         STRING,
    price             FLOAT
) 
DISTRIBUTED BY HASH(item_id1);

INSERT INTO goods1
VALUES
    (1001,"apple",6.5),
    (1002,"pear",8.0),
    (1003,"potato",2.2);


CREATE TABLE order_list1(
    order_id          INT NOT NULL,
    client_id         INT,
    item_id2          INT NOT NULL,
    order_date        DATE NOT NULL,
    check_date        DATE NOT NULL
) 
partition by date_trunc('day', order_date)
DISTRIBUTED BY HASH(order_id);



INSERT INTO order_list1
VALUES
    (10001,101,1001,"2022-03-13", "2022-03-14"),
    (10001,101,1002,"2022-03-13", "2022-03-14"),
    (10002,103,1002,"2022-03-13", "2022-03-14"),
    (10002,103,1003,"2022-03-14", "2022-03-15"),
    (10003,102,1003,"2022-03-14", "2022-03-15"),
    (10003,102,1001,"2022-03-14", "2022-03-15");



CREATE MATERIALIZED VIEW order_mv1
PARTITION BY (`order_date`)
DISTRIBUTED BY HASH(`order_date`)
REFRESH ASYNC START('2022-09-01 10:00:00') EVERY (interval 1 day)
AS SELECT
    order_list1.order_date,
    sum(goods1.price) as total
FROM order_list1 INNER JOIN goods1 ON goods1.item_id1 = order_list1.item_id2
GROUP BY order_list1.order_date;


CREATE MATERIALIZED VIEW order_mv2
PARTITION BY (check_date)
DISTRIBUTED BY HASH(check_date)
REFRESH ASYNC START('2022-09-01 10:00:00') EVERY (interval 1 day)
AS SELECT
    order_list1.check_date,
    sum(goods1.price) as total
FROM order_list1 INNER JOIN goods1 ON goods1.item_id1 = order_list1.item_id2
GROUP BY order_list1.check_date;

ERROR 1064 (HY000): Getting analyzing error. Detail message: Materialized view partition column in partition exp must be base table partition column.

