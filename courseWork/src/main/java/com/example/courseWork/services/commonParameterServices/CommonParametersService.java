package com.example.courseWork.services.commonParameterServices;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParameterRequestDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersRequestDTO;
import com.example.courseWork.DTO.commonParameterDTO.CommonParametersResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.gameDTO.GamesResponseDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.models.gameModel.GameStateParameter;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.repositories.commonParameterRepositories.CommonParametersRepository;
import com.example.courseWork.services.gameServices.GameStateParameterTypesService;
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
            commonParametersRepository.delete(commonParameter);
        }
    }
    @Transactional
    public CommonParameter update(CommonParameterRequestDTO commonParameterRequestDTO, int id){

        Optional<CommonParameter> optionalCommonParameter = commonParametersRepository.findById(id);
        if(optionalCommonParameter.isPresent()){
            CommonParameter updatedCommonParameter = convertToCommonParameter(commonParameterRequestDTO);
            CommonParameter commonParameterToUpdate = optionalCommonParameter.get();

            updatedCommonParameter.setId(commonParameterToUpdate.getId());

            commonParametersRepository.save(updatedCommonParameter);
            return updatedCommonParameter;
        }
        return null;
    }

    public CommonParametersResponseDTO findAll(CommonParametersRequestDTO commonParametersRequestDTO){
        Page<CommonParameter> page = commonParametersRepository.findAll(PageRequest.of(
                commonParametersRequestDTO.getPageNumber() - 1,
                commonParametersRequestDTO.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id")
        ));
        CommonParametersResponseDTO commonParametersResponseDTO = new CommonParametersResponseDTO();

        commonParametersResponseDTO.setItems(page.getContent().stream().map(
                this::constructCommonParameter
        ).toList());
        commonParametersResponseDTO.setTotalCount(page.getTotalElements());

        return commonParametersResponseDTO;
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
        commonParameterDTO.setGameStateParameterTypeDTO(convertToGameStateParameterTypeDTO(commonParameter.getGameStateParameterType()));
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
        commonParameterDTO.setGameStateParameterTypeDTO(gameStateParameterTypeDTO);
        return commonParameterDTO;
    }

    private GameStateParameterTypeDTO convertToGameStateParameterTypeDTO(GameStateParameterType gameStateParameterType){
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }

}
