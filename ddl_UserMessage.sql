CREATE TABLE user_message
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    subject    VARCHAR(255)          NULL,
    content    VARCHAR(255)          NULL,
    created_at datetime              NULL,
    sender_id  BIGINT                NULL,
    `read`     BIT(1)                NOT NULL,
    answered   BIT(1)                NOT NULL,
    CONSTRAINT pk_user_message PRIMARY KEY (id)
);

ALTER TABLE user_message
    ADD CONSTRAINT FK_USER_MESSAGE_ON_SENDER FOREIGN KEY (sender_id) REFERENCES user (id);