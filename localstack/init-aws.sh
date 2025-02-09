#!/bin/bash
awslocal sqs create-queue --region us-east-1  --queue-name local-trigger_processing --attributes VisibilityTimeout=30,MessageRetentionPeriod=345600
awslocal sqs create-queue --region us-east-1  --queue-name update_process-queue2 --attributes VisibilityTimeout=30,MessageRetentionPeriod=345600