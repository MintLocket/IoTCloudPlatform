package helloworld;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class App implements RequestHandler<Object, String> {

    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        String json = ""+input;
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(json);
        JsonElement state = element.getAsJsonObject().get("state");
        JsonElement reported = state.getAsJsonObject().get("reported");
        String sound = reported.getAsJsonObject().get("sound").getAsString();
        double snd = Double.valueOf(sound);

        final String AccessKey="";
        final String SecretKey="";
        final String topicArn="arn:aws:sns:ap-southeast-2:700254074272:Light_Warning";
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(AccessKey, SecretKey);
        AmazonSNS sns = AmazonSNSClientBuilder.standard()
                .withRegion(Regions.AP_SOUTHEAST_2)
                .withCredentials( new AWSStaticCredentialsProvider(awsCreds) )
                .build();

        final String msg = "*Abnormal Situation Alert!!*\n"+ "Abnormal Sound Detected!!";
        final String subject = "Critical Warning";
        if (snd >= 500) {
            PublishRequest publishRequest = new PublishRequest(topicArn, msg, subject);
            PublishResult publishResponse = sns.publish(publishRequest);
        }

        return subject;
    }
}
