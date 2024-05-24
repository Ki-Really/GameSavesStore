package com.example.courseWork.services.commonParameterServices;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParameterRequestDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersRequestDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.repositories.commonParameterRepositories.CommonParametersRepository;
import com.example.courseWork.services.gameServices.GameStateParameterTypesService;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterBadRequestException;
import com.example.courseWork.util.exceptions.commonParameterException.CommonParameterNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CommonParametersService {
    private final CommonParametersRepository commonParametersRepository;
    private final GameStateParameterTypesService gameStateParameterTypesService;
    @Autowired
    public CommonParametersService(CommonParametersRepository commonParametersRepository, GameStateParameterTypesService gameStateParameterTypesService) {
        this.commonParametersRepository = commonParametersRepository;
        this.gameStateParameterTypesService = gameStateParameterTypesService;
    }
    @Transactional
    public CommonParameter save(CommonParameterRequestDTO commonParameterRequestDTO){
        CommonParameter commonParameter = convertToCommonParameter(commonParameterRequestDTO);
        commonParametersRepository.save(commonParameter);
        return commonParameter;
    }
    @Transactional
    public void delete(int id){
        Optional<CommonParameter> optionalCommonParameter = commonParametersRepository.findById(id);
        if(optionalCommonParameter.isPresent()){
            CommonParameter commonParameter = optionalCommonParameter.get();
            commonParameter.getGameStateParameters().forEach(gameStateParameter -> gameStateParameter.setCommonParameter(null));
            commonParametersRepository.delete(commonParameter);
        }else{
            throw new CommonParameterNotFoundException("Common parameter not found with this id!");
        }
    }
    @Transactional
    public CommonParameter update(CommonParameterRequestDTO commonParameterRequestDTO, int id) {
        Optional<CommonParameter> optionalCommonParameter = commonParametersRepository.findById(id);
        if (optionalCommonParameter.isPresent()) {
            CommonParameter commonParameterForValid = findCommonParameterByLabel(commonParameterRequestDTO.getLabel());
            if (commonParameterForValid != null && commonParameterForValid.getId() != id) {
                throw new CommonParameterBadRequestException("Common parameter with this name already exists!", null);
            } else {
                CommonParameter updatedCommonParameter = convertToCommonParameter(commonParameterRequestDTO);
                CommonParameter commonParameterToUpdate = optionalCommonParameter.get();
                updatedCommonParameter.setId(commonParameterToUpdate.getId());
                commonParametersRepository.save(updatedCommonParameter);
                return updatedCommonParameter;
            }
        } else {
            throw new CommonParameterNotFoundException("Common parameter not found with this id!");
        }
    }

    public EntitiesResponseDTO<CommonParameterDTO> findAll(CommonParametersRequestDTO commonParametersRequestDTO){
        Page<CommonParameter> page;
        if (commonParametersRequestDTO.getSearchQuery() != null && !commonParametersRequestDTO.getSearchQuery().isEmpty()) {
            page = commonParametersRepository.findByLabelContainingOrDescriptionContaining(commonParametersRequestDTO.getSearchQuery(),commonParametersRequestDTO.getSearchQuery(),
                    PageRequest.of(
                    commonParametersRequestDTO.getPageNumber() - 1,
                    commonParametersRequestDTO.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "id")
            ));
        }else{
            page = commonParametersRepository.findAll(
                    PageRequest.of(
                            commonParametersRequestDTO.getPageNumber() - 1,
                            commonParametersRequestDTO.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                    ));
        }
        EntitiesResponseDTO<CommonParameterDTO> commonParametersResponseDTO = new EntitiesResponseDTO<>();

        commonParametersResponseDTO.setItems(page.getContent().stream().map(
                this::constructCommonParameter
        ).toList());
        commonParametersResponseDTO.setTotalCount(page.getTotalElements());

        return commonParametersResponseDTO;
    }

    public CommonParameter findById(int id) {
        return commonParametersRepository.findById(id).orElseThrow(()->new CommonParameterNotFoundException("Common parameter with this id was not found!"));
    }

    private CommonParameter convertToCommonParameter(CommonParameterRequestDTO commonParameterRequestDTO){
        CommonParameter commonParameter = new CommonParameter();
        commonParameter.setLabel(commonParameterRequestDTO.getLabel());
        commonParameter.setDescription(commonParameterRequestDTO.getDescription());
        GameStateParameterType gameStateParameterType = gameStateParameterTypesService.findById(commonParameterRequestDTO.getGameStateParameterTypeId());
        commonParameter.setGameStateParameterType(gameStateParameterType);
        gameStateParameterType.getCommonParameters().add(commonParameter);
        return commonParameter;
    }

    public CommonParameterDTO constructCommonParameterDTO(CommonParameter commonParameter){
        CommonParameterDTO commonParameterDTO = new CommonParameterDTO();
        commonParameterDTO.setLabel(commonParameter.getLabel());
        commonParameterDTO.setDescription(commonParameter.getDescription());
        commonParameterDTO.setId(commonParameter.getId());
        commonParameterDTO.setType(convertToGameStateParameterTypeDTO(commonParameter.getGameStateParameterType()));
        return commonParameterDTO;
    }

    private CommonParameterDTO constructCommonParameter(CommonParameter commonParameter){
        CommonParameterDTO commonParameterDTO = new CommonParameterDTO();
        commonParameterDTO.setLabel(commonParameter.getLabel());
        commonParameterDTO.setDescription(commonParameter.getDescription());
        commonParameterDTO.setId(commonParameter.getId());

        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(commonParameter.getGameStateParameterType().getId());
        gameStateParameterTypeDTO.setType(commonParameter.getGameStateParameterType().getType());
        commonParameterDTO.setType(gameStateParameterTypeDTO);
        return commonParameterDTO;
    }

    private GameStateParameterTypeDTO convertToGameStateParameterTypeDTO(GameStateParameterType gameStateParameterType){
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }

    public CommonParameter findCommonParameterByLabel(String label){
        Optional<CommonParameter> optionalCommonParameter = commonParametersRepository.findByLabel(label);
        return optionalCommonParameter.orElse(null);
    }
}
