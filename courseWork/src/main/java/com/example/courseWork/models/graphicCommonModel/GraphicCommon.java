package com.example.courseWork.models.graphicCommonModel;

import com.example.courseWork.models.commonParameters.CommonParameter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(schema ="cloud_game_saves",name = "graphic_common")
public class GraphicCommon {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Type of graphic should not be empty!")
    @Column(name="visual_type")
    private String visualType;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name ="fk_common_parameter_id",referencedColumnName = "id")
    private CommonParameter commonParameter;

    public GraphicCommon(String visualType) {
        this.visualType = visualType;
    }

    public GraphicCommon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVisualType() {
        return visualType;
    }

    public void setVisualType(String visualType) {
        this.visualType = visualType;
    }

    public CommonParameter getCommonParameter() {
        return commonParameter;
    }

    public void setCommonParameter(CommonParameter commonParameter) {
        this.commonParameter = commonParameter;
    }
}
