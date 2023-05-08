import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.ec2.model.Reservation;
import software.amazon.awssdk.services.ec2.model.Instance;
import software.amazon.awssdk.services.ec2.model.Filter;

public class Example {
    public static void main(final String[] args) {
        DefaultCredentialsProvider credentialsProviderChain = DefaultCredentialsProvider.create(); // AWSStaticCredentialsProvider

        RetryPolicy retryPolicy = RetryPolicy.builder()
                .numRetries(10)
                .build();

        Ec2Client client = Ec2Client.builder()
                .httpClientBuilder(ApacheHttpClient.builder())
                .credentialsProvider(credentialsProviderChain)
                .build();

        try {
            DescribeInstancesRequest describeInstancesRequest = DescribeInstancesRequest.builder()
                    .filters(Filter.builder().name("instance-state-name").values("running", "pending", "stopped").build())
                    .build();

            DescribeInstancesResponse describeInstancesResponse = client.describeInstances(describeInstancesRequest);
            for (final Reservation reservation : describeInstancesResponse.reservations()) {
                System.out.println(reservation.reservationId());
                for (final Instance instance : reservation.instances()) {
                    System.out.println("\t" + instance.publicDnsName());
                }
            }
        } finally {
            client.close();
        }
    }
}
