package com.devtence.will.dev.models;


/**
 * base class for all the models of LP
 * Created by sorcerer on 7/18/15.
 */
public abstract class BaseModel {

    /**mvz
     * todos los modelos deben ser validados para la insercion o mod
     * @throws Exception
     */
    public abstract void validate() throws Exception;

    /**
     * funcion para salvar los objetos a datastorage
     */
    protected void save(){
            DbObjectify.ofy().save().entity(this).now();
    }

    protected void delete(){
        DbObjectify.ofy().delete().entity(this).now();
    }

    //TODO creat abstract class validateAndSave()

}
