
package interpreter.analex;

import interpreter.analex.interfaces.ITokenEventListener;
import interpreter.analex.models.TokenCommand;
import interpreter.analex.utils.Token;
import interpreter.events.TokenEvent;


public class Interpreter implements Runnable{
    private ITokenEventListener listener;
    private Analex analex;
    
    private String command;
    private String sender;
    
    public Interpreter(String command, String sender) {
        this.command = command;
        this.sender = sender;        
    }

    public ITokenEventListener getListener() {
        return listener;
    }

    public void setListener(ITokenEventListener listener) {
        this.listener = listener;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
    
    private void filterEvent(TokenCommand token_command){
        TokenEvent token_event = new TokenEvent(this, sender);        
        token_event.setAction(token_command.getAction());
        
        int count_params = token_command.countParams();
        for(int i = 0; i < count_params; i++){
            int pos = token_command.getParams(i);
            token_event.addParams(analex.getParam(pos));
        }
        
        switch(token_command.getName()){
            case Token.HELP:
                listener.help(token_event);
                break;
            case Token.HELP_FLUJO:
                listener.helpFlujo(token_event);
                break;
            case Token.ROL:
                listener.rol(token_event);
                break;
            case Token.METODOPAGO:
                listener.metodoPago(token_event);
                break;
            case Token.PLANPAGO:
                listener.planPago(token_event);
                break;
            case Token.FRANJAH:
                listener.franjaHoraria(token_event);
                break;
            case Token.TIPOVEHICULO:
                listener.tipoVehiculo(token_event);
                break;
            case Token.TIPOCURSO:
                listener.tipoCurso(token_event);
                break;
            case Token.USUARIO:
                listener.usuario(token_event);
                break;
            case Token.VEHICULO:
                listener.vehiculo(token_event);
                break;
            case Token.CURSO:
                listener.curso(token_event);
                break;
            case Token.INSCRIPCION:
                listener.inscripcion(token_event);
                break;
            case Token.CERTIFICACION:
                listener.certificacion(token_event);
                break;
            case Token.PAGO:
                listener.pago(token_event);
                break;
            case Token.REPORTE:
                listener.reporte(token_event);
                break;
            case Token.RESERVA:
                listener.reserva(token_event);
                break;
        }
        
    }
    
    private void tokenError(Token token, String error){
        TokenEvent token_event = new TokenEvent(this, sender);
        token_event.setAction(token.getAttribute());
        token_event.addParams(command);
        token_event.addParams(error);
        listener.error(token_event);
    }
    
    @Override
    public void run() {
        analex = new Analex(command);
        TokenCommand token_command = new TokenCommand();
        Token token;
        
        //while(analex.Preanalisis() != null) {
            //token = analex.Preanalisis();
            //if (token.getName() == Token.END && token.getName() == Token.ERROR) {
                //break;
            //}
        //}
        
        while((token = analex.Preanalisis()).getName() != Token.END && token.getName() != Token.ERROR){
            if(token.getName() == Token.CU){
                token_command.setName(token.getAttribute());
            } else if(token.getName() == Token.ACTION){
                token_command.setAction(token.getAttribute());
            } else if(token.getName() == Token.COMPOUND){
                // Token compuesto: resuelve CU y acción de una sola palabra (ej. INSCAT)
                token_command.setName(Token.getCompoundCU(token.getAttribute()));
                token_command.setAction(Token.getCompoundAction(token.getAttribute()));
            } else if(token.getName() == Token.PARAMS){
                token_command.addParams(token.getAttribute());
            }
            analex.next();
        }
        
        if(token.getName() == Token.END){
            filterEvent(token_command);// se analizo el comando con exito
        } else if(token.getName() == Token.ERROR){
            tokenError(token, analex.lexeme()); // se produjo un error en el analisis
        }
        
    }
}
