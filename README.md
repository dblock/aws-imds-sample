# AWS Instance Metadata

Instance metadata is data about your instance that you can use to configure or manage the running instance.
See https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-instance-metadata.html for more information.
This sample fetches instance metadata with AWS SDK [v1](v1) and [v2](v2).

## Running

```
export AWS_ACCESS_KEY_ID=
export AWS_SECRET_ACCESS_KEY=
export AWS_SESSION_TOKEN=
export AWS_REGION=us-west-2

cd v2
mvn install
mvn compile exec:java -Dexec.mainClass="Example"
```

## License 

This project is licensed under the [Apache v2.0 License](LICENSE.txt).

## Copyright

Copyright OpenSearch Contributors. See [NOTICE](NOTICE.txt) for details.
