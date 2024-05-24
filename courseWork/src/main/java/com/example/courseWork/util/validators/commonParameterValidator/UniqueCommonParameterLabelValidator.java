package com.example.courseWork.util.validators.commonParameterValidator;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterRequestDTO;
import com.example.courseWork.DTO.gameDTO.GameRequestDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueCommonParameterLabelValidator implements Validator {
    private final CommonParametersService commonParametersService;

    @Autowired
    public UniqueCommonParameterLabelValidator(CommonParametersService commonParametersService) {
        this.commonParametersService = commonParametersService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CommonParameter.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CommonParameterRequestDTO commonParameterRequestDTO = (CommonParameterRequestDTO) target;
        CommonParameter commonParameter = commonParametersService.findCommonParameterByLabel(commonParameterRequestDTO.getLabel());
        if(commonParameter!=null){
            errors.rejectValue("label","","Common parameter with this label " +
                    "already exists!");
        }
    }
}
