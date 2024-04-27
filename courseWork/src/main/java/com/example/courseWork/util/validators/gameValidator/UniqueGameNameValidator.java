package com.example.courseWork.util.validators.gameValidator;

import com.example.courseWork.models.gameModel.Game;
import com.example.courseWork.services.gameServices.GamesService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UniqueGameNameValidator implements Validator {
    private final GamesService gamesService;

    public UniqueGameNameValidator(GamesService gamesService) {
        this.gamesService = gamesService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Game.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Game game = (Game)target;
        if(gamesService.checkGamePresentByName(game.getName())!=null){
            errors.rejectValue("name","","Game with this name already exists!");
        }
    }
}
