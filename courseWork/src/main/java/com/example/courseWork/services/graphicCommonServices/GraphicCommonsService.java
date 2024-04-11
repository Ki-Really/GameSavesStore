package com.example.courseWork.services.graphicCommonServices;

import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonRequestDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.graphicCommonModel.GraphicCommon;
import com.example.courseWork.repositories.graphicCommonRepositories.GraphicCommonsRepository;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class GraphicCommonsService {
    private final GraphicCommonsRepository graphicCommonsRepository;
    private final CommonParametersService commonParametersService;
    @Autowired
    public GraphicCommonsService(GraphicCommonsRepository graphicCommonsRepository, CommonParametersService commonParametersService) {
        this.graphicCommonsRepository = graphicCommonsRepository;
        this.commonParametersService = commonParametersService;
    }
    @Transactional
    public GraphicCommonDTO save(GraphicCommonRequestDTO graphicCommonRequestDTO) {
        GraphicCommon graphicCommon = convertToGraphicCommon(graphicCommonRequestDTO);
        graphicCommonsRepository.save(graphicCommon);
        return constructGraphicCommonDTO(graphicCommon);
    }
    @Transactional
    public void delete(int id){
        graphicCommonsRepository.deleteById(id);
    }
    @Transactional
    public GraphicCommonDTO update(int id, GraphicCommonRequestDTO graphicCommonRequestDTO){
        Optional<GraphicCommon> optionalGraphicCommon = graphicCommonsRepository.findById(id);
        if(optionalGraphicCommon.isPresent()){
            GraphicCommon graphicCommonToUpdate = optionalGraphicCommon.get();
            GraphicCommon updatedGraphicCommon = convertToGraphicCommon(graphicCommonRequestDTO);
            updatedGraphicCommon.setId(graphicCommonToUpdate.getId());
            graphicCommonsRepository.save(updatedGraphicCommon);
            return constructGraphicCommonDTO(updatedGraphicCommon);
        }
        return null;
    }

    /*public EntitiesResponseDTO<GraphicCommonDTO> findAll(GraphicCommonsRequestDTO graphicCommonsRequestDTO){
        Page<GraphicCommon> page;
        if (graphicCommonsRequestDTO.getSearchQuery() != null && !graphicCommonsRequestDTO.getSearchQuery().isEmpty()) {
            page = graphicCommonsRepository.findByVisualTypeContaining(graphicCommonsRequestDTO.getSearchQuery(),
                    PageRequest.of(
                            graphicCommonsRequestDTO.getPageNumber() - 1,
                            graphicCommonsRequestDTO.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                    ));
        }else{
            page = graphicCommonsRepository.findAll(
                    PageRequest.of(
                            graphicCommonsRequestDTO.getPageNumber() - 1,
                            graphicCommonsRequestDTO.getPageSize(),
                            Sort.by(Sort.Direction.DESC, "id")
                    ));
        }
        EntitiesResponseDTO<GraphicCommonDTO> graphicCommonsResponseDTO = new EntitiesResponseDTO<>();

        graphicCommonsResponseDTO.setItems(page.getContent().stream().map(
                this::constructGraphicCommonDTO
        ).toList());
        graphicCommonsResponseDTO.setTotalCount(page.getTotalElements());

        return graphicCommonsResponseDTO;
    }*/



    private GraphicCommon convertToGraphicCommon(GraphicCommonRequestDTO graphicCommonRequestDTO) {
        GraphicCommon graphicCommon = new GraphicCommon();
        graphicCommon.setVisualType(graphicCommonRequestDTO.getVisualType());
        CommonParameter commonParameter = commonParametersService.findById(graphicCommonRequestDTO.getCommonParameterId());
        graphicCommon.setCommonParameter(commonParameter);
        commonParameter.getGraphicCommons().add(graphicCommon);
        return graphicCommon;
    }
    private GraphicCommonDTO constructGraphicCommonDTO(GraphicCommon graphicCommon) {
        GraphicCommonDTO graphicCommonDTO = new GraphicCommonDTO();
        graphicCommonDTO.setId(graphicCommon.getId());
        graphicCommonDTO.setVisualType(graphicCommon.getVisualType());
        graphicCommonDTO.setCommonParameterId(graphicCommon.getCommonParameter().getId());
        return graphicCommonDTO;
    }

}
