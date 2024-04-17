package com.example.courseWork.services.graphicCommonServices;

import com.example.courseWork.DTO.commonParameterDTO.CommonParameterDTO;
import com.example.courseWork.DTO.entityDTO.EntitiesResponseDTO;
import com.example.courseWork.DTO.gameDTO.GameStateParameterTypeDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonRequestDTO;
import com.example.courseWork.DTO.graphicCommonDTO.GraphicCommonsRequestDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.CommonHistogramDataDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.CommonPieChartDataDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.GraphicCommonHistogramTimeResponseDataDTO;
import com.example.courseWork.DTO.graphicCommonDataDTO.GraphicCommonPieChartGenderResponseDataDTO;
import com.example.courseWork.models.commonParameters.CommonParameter;
import com.example.courseWork.models.gameModel.GameStateParameter;
import com.example.courseWork.models.gameModel.GameStateParameterType;
import com.example.courseWork.models.graphicCommonModel.GraphicCommon;
import com.example.courseWork.repositories.graphicCommonRepositories.GraphicCommonsRepository;
import com.example.courseWork.services.commonParameterServices.CommonParametersService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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

    public EntitiesResponseDTO<GraphicCommonDTO> findAll(GraphicCommonsRequestDTO graphicCommonsRequestDTO){
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
    }

    public GraphicCommonDTO findById(int id) {
        Optional<GraphicCommon> optionalGraphicCommon = graphicCommonsRepository.findById(id);
        GraphicCommon graphicCommon = optionalGraphicCommon.orElseThrow();

        return constructGraphicCommonDTO(graphicCommon);
    }

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
 /*   public GraphicCommon graphicCommon(int id){
        graphicCommonsRepository.findById(id);
    }*/
/*    public String getVisualType(int id){
        GraphicCommonHistogramTimeResponseDataDTO graphicCommonHistogramTimeResponseDataDTO = new GraphicCommonHistogramTimeResponseDataDTO();
        Optional<GraphicCommon> optionalGraphicCommon = graphicCommonsRepository.findById(id);

        if(optionalGraphicCommon.isPresent()){

            GraphicCommon graphicCommon = optionalGraphicCommon.get();
            String visualType = graphicCommon.getVisualType();
            if(visualType.equals("histogram")){
                return "histogram";
            }
            if(visualType.equals("pie_chart")){
                return "pie_chart";
            }
        }
        return null;
    }*/


    public GraphicCommonHistogramTimeResponseDataDTO getHistogramTimeData(int id){
        Optional<GraphicCommon> optionalGraphicCommon = graphicCommonsRepository.findById(id);

        if(optionalGraphicCommon.isPresent()){
            GraphicCommon graphicCommon = optionalGraphicCommon.get();
            GraphicCommonHistogramTimeResponseDataDTO graphicCommonResponseDataDTO = new GraphicCommonHistogramTimeResponseDataDTO();


            List<String> extractedValues = new LinkedList<>();
            GameStateParameter gameStateParameter;

            int gameStateParametersSize = graphicCommon.getCommonParameter().getGameStateParameters().size();
            int gameStateValuesSize;
            for(int i = 0; i<gameStateParametersSize;i++){
                gameStateParameter = graphicCommon.getCommonParameter().getGameStateParameters().get(i);
                gameStateValuesSize = gameStateParameter.getGameStateValues().size();
                for(int j = 0; j<gameStateValuesSize;j++){
                    extractedValues.add(String.valueOf(gameStateParameter.getGameStateValues().get(j).getValue()));
                }
            }
            List<Double> data = extractedValues.stream().map(Double::parseDouble).toList();

            double minValue = data.stream().min(Double::compareTo).orElse(Double.MIN_VALUE);
            double maxValue = data.stream().max(Double::compareTo).orElse(Double.MAX_VALUE);

            double partSize = (maxValue - minValue + 1)/10;

            List<CommonHistogramDataDTO> commonHistogramDataDTOList = new LinkedList<>();

            for(double i = minValue; i<=maxValue;i+=partSize){
                int counter = 0;
                for(double value : data){
                    if(value>=i && value < i+partSize){
                        counter++;
                    }
                }
                commonHistogramDataDTOList.add(new CommonHistogramDataDTO(i,i + partSize - 1,counter));
            }

            graphicCommonResponseDataDTO.setId(graphicCommon.getId());
            graphicCommonResponseDataDTO.setVisualType(graphicCommon.getVisualType());
            graphicCommonResponseDataDTO.setCommonParameter(convertToCommonParameterDTO(graphicCommon.getCommonParameter()));
            graphicCommonResponseDataDTO.setData(commonHistogramDataDTOList);
            return graphicCommonResponseDataDTO;
        }
        else{
            return null;
        }
    }

    public GraphicCommonPieChartGenderResponseDataDTO getPieChartGenderData(int id){
        Optional<GraphicCommon> optionalGraphicCommon = graphicCommonsRepository.findById(id);

        if(optionalGraphicCommon.isPresent()){
            GraphicCommon graphicCommon = optionalGraphicCommon.get();
            GraphicCommonPieChartGenderResponseDataDTO graphicCommonResponseDataDTO = new GraphicCommonPieChartGenderResponseDataDTO();
            graphicCommonResponseDataDTO.setVisualType(graphicCommon.getVisualType());
            graphicCommonResponseDataDTO.setCommonParameter(convertToCommonParameterDTO(graphicCommon.getCommonParameter()));

            List<String> extractedValues = new LinkedList<>();
            GameStateParameter gameStateParameter;

            int gameStateParametersSize = graphicCommon.getCommonParameter().getGameStateParameters().size();
            int gameStateValuesSize;
            for(int i = 0; i<gameStateParametersSize;i++){
                gameStateParameter = graphicCommon.getCommonParameter().getGameStateParameters().get(i);
                gameStateValuesSize = gameStateParameter.getGameStateValues().size();
                for(int j = 0; j<gameStateValuesSize;j++){
                    extractedValues.add(String.valueOf(gameStateParameter.getGameStateValues().get(j).getValue()));
                }
            }
            int commonSizeElements = extractedValues.size();
            int maleCounter = 0;
            for(String value : extractedValues){
                if(value.equals("male")){
                    maleCounter ++;
                }
            }
            double percentageOfMen = ((double)maleCounter / commonSizeElements) * 100;
            double percentageOfWomen = 100 - percentageOfMen;
            List<CommonPieChartDataDTO> commonList = new LinkedList<>();
            CommonPieChartDataDTO commonPieChartDataWomenDTO = new CommonPieChartDataDTO((int)percentageOfWomen,"women");
            CommonPieChartDataDTO commonPieChartDataMenDTO = new CommonPieChartDataDTO((int)percentageOfMen,"men");
            commonList.add(commonPieChartDataWomenDTO);
            commonList.add(commonPieChartDataMenDTO);

            graphicCommonResponseDataDTO.setId(graphicCommon.getId());
            graphicCommonResponseDataDTO.setVisualType(graphicCommon.getVisualType());
            graphicCommonResponseDataDTO.setCommonParameter(convertToCommonParameterDTO(graphicCommon.getCommonParameter()));
            graphicCommonResponseDataDTO.setData(commonList);
            System.out.println(graphicCommonResponseDataDTO);
            return graphicCommonResponseDataDTO;
        }
        else {
            return null;
        }
    }

    private CommonParameterDTO convertToCommonParameterDTO(CommonParameter commonParameter){
        CommonParameterDTO commonParameterDTO = new CommonParameterDTO();
        commonParameterDTO.setId(commonParameter.getId());
        commonParameterDTO.setLabel(commonParameter.getLabel());
        commonParameterDTO.setType(convertToGameStateParameterDTO(commonParameter.getGameStateParameterType()));
        commonParameterDTO.setDescription(commonParameter.getDescription());
        return commonParameterDTO;
    }
    private GameStateParameterTypeDTO convertToGameStateParameterDTO(GameStateParameterType gameStateParameterType){
        GameStateParameterTypeDTO gameStateParameterTypeDTO = new GameStateParameterTypeDTO();
        gameStateParameterTypeDTO.setId(gameStateParameterType.getId());
        gameStateParameterTypeDTO.setType(gameStateParameterType.getType());
        return gameStateParameterTypeDTO;
    }

}
