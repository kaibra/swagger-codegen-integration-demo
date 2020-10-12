#/bin/bash
JAVA="java"

mvn clean package -DskipTests=true

EXECUTABLE=$(find target -name swagger-codegen-integration-*.jar | head -n 1)
TEST_DATA=src/test/resources/specs/petstore_v3.json
OUTPUT_DIR=generated-code

if [ -d $OUTPUT_DIR ]; then
  echo "Found dir"
  rm -rf $OUTPUT_DIR/*
fi

$JAVA -jar  $EXECUTABLE  $TEST_DATA $OUTPUT_DIR

