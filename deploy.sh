#!/bin/bash

set -x

BUCKET=ingenieux-images
KEY=/bpservice/bpservice.zip
ROLE_NAME=lambda_basic_execution

function lookup_role {
  ROLE=$(aws iam list-roles | jq -r .[][].Arn | grep role/$ROLE_NAME)

  if [ "x" == "x$ROLE" ]; then
    echo "Role name not found: $ROLE_NAME"
    exit 1
  fi
}

ROLE=$(lookup_role $ROLE_NAME)

function deploy_function_code {
  FUNCTION_NAME=$1
  FUNCTION_HANDLER=$2
  FUNCTION_ROLE=$3

  aws lambda update-function-code --function-name $FUNCTION_NAME --s3-bucket $BUCKET --s3-key $KEY ||
    aws lambda create-function --function-name $FUNCTION_NAME --runtime java8 --role $FUNCTION_ROLE --handler $FUNCTION_HANDLER --code S3Bucket=$BUCKET,S3Key=$KEY --timeout 60 --memory-size 128
}

aws s3 cp target/bpservice.jar s3://$BUCKET/$KEY

deploy_function_code bps_opRawHandler io.ingenieux.sample.Handlers::rawHandler $ROLE
deploy_function_code bps_opArrayHandler io.ingenieux.sample.Handlers::arrayHandler $ROLE
deploy_function_code bps_opPojoHandler io.ingenieux.sample.Handlers::pojoHandler $ROLE
deploy_function_code bps_opDisplayProperties io.ingenieux.sample.Handlers::displayPropertiesHandler $ROLE


