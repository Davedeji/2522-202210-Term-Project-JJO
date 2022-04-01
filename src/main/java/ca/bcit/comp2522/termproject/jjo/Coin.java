package ca.bcit.comp2522.termproject.jjo;

import com.almasb.fxgl.entity.Entity;

import java.io.Serializable;

public class Coin implements Serializable {
    private Entity entity;

    public Coin(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }


}
