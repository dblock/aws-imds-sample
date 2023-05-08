import com.amazonaws.Protocol;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
public class Example {

    public static void main(final String[] args) {
        // custom endpoint
        // EndpointConfiguration endpointConfiguration = new EndpointConfiguration("TODO", null);
        // auth
        DefaultAWSCredentialsProviderChain credentialsProviderChain = DefaultAWSCredentialsProviderChain.getInstance(); // AWSStaticCredentialsProvider
        // client
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setProtocol(Protocol.HTTPS);
        clientConfiguration.setResponseMetadataCacheSize(0);
        clientConfiguration.setMaxErrorRetry(10);
        // clientConfiguration.setSocketTimeout(ClientConfiguration.DEFAULT_SOCKET_TIMEOUT);
        // clientConfiguration.setProxyHost();
        // clientConfiguration.setProxyPort();
        // clientConfiguration.setProxyUsername();
        // clientConfiguration.setProxyPassword();
        AmazonEC2 client = AmazonEC2ClientBuilder.standard()
                // .withEndpointConfiguration(endpointConfiguration)
                .withClientConfiguration(clientConfiguration)
                .withCredentials(credentialsProviderChain)
                .build();

        try {
            DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest()
                    .withFilters(new Filter("instance-state-name").withValues("running", "pending", "stopped"));
            // .withFilters(new Filter("tag:" + ...).withValues(...));
            // .withFilters(new Filter("availability-zone").withValues(...));

            DescribeInstancesResult describeInstancesResult = client.describeInstances(describeInstancesRequest);
            for (final Reservation reservation : describeInstancesResult.getReservations()) {
                System.out.println(reservation.getReservationId());
                for (final Instance instance : reservation.getInstances()) {
                    System.out.println("\t" + instance.getPublicDnsName());
                }
            }
        } finally {
            client.shutdown();
        }
    }
}
