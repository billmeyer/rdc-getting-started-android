#!/bin/bash

function push_to_sauce () {
  FILE=$1
  BASENAME=`basename ${FILE}`
  echo "Pushing ${FILE} to sauce-storage:${BASENAME}"

  curl -u "${SAUCE_USERNAME}:${SAUCE_ACCESS_KEY}" -X POST -w "%{http_code}\n" \
        -H "Content-Type: application/octet-stream" \
        https://app.saucelabs.com/rest/v1/storage/${SAUCE_USERNAME}/${BASENAME}?overwrite=true \
        --data-binary @${FILE}
}

push_to_sauce 'LoanCalc.apk'
