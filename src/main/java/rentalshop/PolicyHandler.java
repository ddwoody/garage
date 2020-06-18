package rentalshop;

import rentalshop.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    BikeRepository bikeRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBikeRented_ChangeStatus(@Payload BikeRented bikeRented){

        if(bikeRented.isMe()){
            System.out.println("##### listener ChangeStatus : " + bikeRented.toJson());

            //자전거 상태를 사용중으로 변경
            bikeRepository.findById(bikeRented.getBikeId())
                    .ifPresent(
                            bike -> {
                                bike.setBikeStatus("OCCUPIED");
                                bikeRepository.save(bike);
                            }
                    )
            ;
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverBikeReturned_ChangeStatus(@Payload BikeReturned bikeReturned){

        if(bikeReturned.isMe()){
            System.out.println("##### listener ChangeStatus : " + bikeReturned.toJson());

            //자전거 상태를 사용가능으로 변경
            bikeRepository.findById(bikeReturned.getBikeId())
                    .ifPresent(
                            bike -> {
                                bike.setBikeStatus("AVAILABLE");
                                bikeRepository.save(bike);
                            }
                    )
            ;
        }
    }

}
