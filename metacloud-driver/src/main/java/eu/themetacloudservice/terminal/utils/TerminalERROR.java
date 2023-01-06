package eu.themetacloudservice.terminal.utils;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TerminalERROR extends ByteArrayOutputStream {





    @Override
    public void flush(){
        final var input = this.toString();
        this.reset();
        if (input != null && !input.isEmpty()) {
            String[] inputs = input.split("\n");
            for (String inputss : inputs){
                Driver.getInstance().getTerminalDriver().log(Type.ERROR, inputss);
            }
        }
    }
}
