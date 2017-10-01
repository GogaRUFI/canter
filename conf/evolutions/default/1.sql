# schemas

# --- !Ups

CREATE TABLE "product" (
    "id" INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    "name" VARCHAR(16) NOT NULL,
    "code" VARCHAR(16) NOT NULL,
    "category" VARCHAR(16) NOT NULL,
    "price" BIGINT(20) NOT NULL,
    "createdAt" BIGINT(20) NOT NULL,
    "modifiedAt" BIGINT(20) DEFAULT NULL,
    "removedAt" BIGINT(20) DEFAULT NULL
);

# --- !Downs

DROP TABLE "product";