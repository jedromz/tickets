package pl.kurs.tickets.model.searchcriteria;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PersonSearchCriteria {
    private String pesel;
    private String firstname;
    private String lastname;
    private String email;
//
//    public Predicate toPredicate() {
//        List<BooleanExpression> conditions = new ArrayList<>();
//        if(pesel!=null){
//            conditions.add(QPerson)
//        }
//    }
}
