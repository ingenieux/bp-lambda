#!/bin/bash

set -x

ROLE=arn:aws:iam::235368163414:role/lambda_basic_execution
BUCKET=ingenieux-images
KEY=/bpservice/bpservice.zip

function deploy_function_code {
  FUNCTION_NAME=$1
  FUNCTION_HANDLER=$2
  FUNCTION_ROLE=$3

  aws lambda update-function-code --function-name $FUNCTION_NAME --s3-bucket $BUCKET --s3-key $KEY ||
    aws lambda create-function --function-name $FUNCTION_NAME --runtime java8 --role $FUNCTION_ROLE --handler $FUNCTION_HANDLER --code S3Bucket=$BUCKET,S3Key=$KEY --timeout 60 --memory-size 128
}

aws s3 cp target/bpservice.jar s3://$BUCKET/$KEY

deploy_function_code opDefaultHandler io.ingenieux.sample.App::defaultHandler $ROLE

