package com.demoapp;

import com.demoapp.resource.MovieResource;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoappApplicationTests {

        @Autowired
        MovieResource movieResource;
        
	@Test
	void contextLoads() {
            intervalTest();
	}
        
        public void intervalTest() {
            Map producersIntervals = movieResource.getProducersIntervals();
            
            assertEquals(!producersIntervals.isEmpty(), true);
            testProducer("min", producersIntervals);
            testProducer("max", producersIntervals);
        }
        
        public void testProducer(String intervalType, Map intervals) {
            List producersList = (List) intervals.get(intervalType);
            for (Object producerMap : producersList) {
                Map producerResult = (Map)producerMap;
                assertEquals(
                    (Integer)producerResult.get("interval"), 
                    (Integer)producerResult.get("followingWin") - (Integer)producerResult.get("previousWin")
                );
            }
        }

}
