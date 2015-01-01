package tonius.neiintegration.mcforge;

import net.minecraftforge.common.MinecraftForge;
import tonius.neiintegration.IntegrationBase;
import tonius.neiintegration.mcforge.dumpers.MCForgeDumpers;

public class MCForgeIntegration extends IntegrationBase {
    
    public MCForgeIntegration() {
        MinecraftForge.EVENT_BUS.register(new MCForgeTooltipHandler());
    }
    
    @Override
    public String getName() {
        return "Minecraft / Forge";
    }
    
    @Override
    public boolean isValid() {
        return true;
    }
    
    @Override
    public void loadConfig() {
        registerHandler(new RecipeHandlerFluidRegistry());
        
        MCForgeDumpers.loadConfig();
    }
    
}