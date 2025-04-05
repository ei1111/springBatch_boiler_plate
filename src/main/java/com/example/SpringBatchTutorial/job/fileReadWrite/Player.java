package com.example.SpringBatchTutorial.job.fileReadWrite;

import lombok.Data;
import lombok.ToString;
import org.springframework.batch.item.file.transform.FieldSet;

@Data
@ToString
public class Player {
    private String ID;
    private String lastName;
    private String firstName;
    private String position;
    private int birthYear;
    private int debutYear;

    public Player(FieldSet fieldSet) {
        ID = fieldSet.readString(0);
        lastName = fieldSet.readString(1);
        firstName = fieldSet.readString(2);
        position = fieldSet.readString(3);
        birthYear = fieldSet.readInt(4);
        debutYear = fieldSet.readInt(5);
    }
}
