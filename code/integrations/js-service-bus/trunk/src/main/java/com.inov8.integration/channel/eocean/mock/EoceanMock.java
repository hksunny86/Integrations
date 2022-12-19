package com.inov8.integration.channel.eocean.mock;

public class EoceanMock {
    public String eoceanResp (){
        String res = "<Messages>\n" +
                "    <Message>\n" +
                "        <MessageID>ea4d9e5a-35f5-4662-96f6-826fda7161d2</MessageID>\n" +
                "        <Date>12/13/2021 12:22:16 PM</Date>\n" +
                "        <Text>Jskjsnckhlbsa;khbdc;khbs;dkahcb.khdcbxbascljqhwb;asdc;xl;///;clmnsdvc.jnsa/l</Text>\n" +
                "        <receiver>923200460403</receiver>\n" +
                "        <ShortCode>9991</ShortCode>\n" +
                "        <status>accepted</status>\n" +
                "        <statuscode>200</statuscode>\n" +
                "    </Message>\n" +
                "</Messages>";
        return res;
    }
}
