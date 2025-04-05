package com.example.SpringBatchTutorial.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamEmptyCheck implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");

        if (!StringUtils.hasText(fileName)) {
            throw new JobParametersInvalidException("fileName 값이 있어야 합니다");
        }
    }
}
