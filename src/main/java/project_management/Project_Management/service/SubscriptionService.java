package project_management.Project_Management.service;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project_management.Project_Management.model.PlayType;
import project_management.Project_Management.model.Subscription;
import project_management.Project_Management.model.User;
import project_management.Project_Management.repository.SubscriptionRepository;

import java.time.LocalDate;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserService userService;

    public Subscription create(User user){
        Subscription subscription=new Subscription();
        subscription.setUser(user);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(12));
        subscription.setValid(true);
        subscription.setPlayType(PlayType.FREE);

        return subscriptionRepository.save(subscription);

    }

    public Subscription getUserSubscription(ObjectId id){
           Subscription subscription= subscriptionRepository.findByUserId(id);
           if(!isValid(subscription)){
                subscription.setPlayType(PlayType.FREE);
                subscription.setEndDate(LocalDate.now().plusMonths(12));
                subscription.setStartDate(LocalDate.now());
           }

           return subscription;
    }

    public Subscription updateSubscription(ObjectId userId, PlayType playType){
         Subscription subscription=subscriptionRepository.findByUserId(userId);
         subscription.setPlayType(playType);
         subscription.setStartDate(LocalDate.now());
         if(subscription.getPlayType().equals(PlayType.ANNUALLY)){
               subscription.setEndDate(LocalDate.now().plusMonths(12));
         }
         else{
             subscription.setEndDate(LocalDate.now().plusMonths(1));
         }
         return  subscriptionRepository.save(subscription);
    }

    public boolean isValid(Subscription subscription){
         if(subscription.getPlayType().equals(PlayType.FREE)){
               return true;
         }

         LocalDate endDate=subscription.getEndDate();
         LocalDate current_date=LocalDate.now();

         return endDate.isAfter(current_date) || endDate.isEqual(current_date);
    }
}
