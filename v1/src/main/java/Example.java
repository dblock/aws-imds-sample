import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import com.amazonaws.services.ec2.model.*;
import com.amazonaws.util.EC2MetadataUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Example {
    static void getCurrentAvailabilityZone() {
        try {
            String azMetadataUrl = EC2MetadataUtils.getHostAddressForEC2MetadataService() + "/latest/meta-data/placement/availability-zone";
            final URL url = new URL(azMetadataUrl);
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setConnectTimeout(2000);
            InputStream in = urlConnection.getInputStream();
            BufferedReader urlReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            final String metadataResult = urlReader.readLine();
            System.out.println("Current instance availability zone: " + metadataResult);
        } catch(IOException ex) {
            System.err.println("Error getting current instance availability zone: " + ex.getMessage());
        }
    }

    public static void main(final String[] args) {
        getCurrentAvailabilityZone();

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
