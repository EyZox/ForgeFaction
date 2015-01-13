package fr.eyzox.forgefaction;

import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IChatComponent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import fr.eyzox.forgefaction.block.ModBlocks;
import fr.eyzox.forgefaction.command.CreateFaction;
import fr.eyzox.forgefaction.command.InvitePlayer;
import fr.eyzox.forgefaction.command.KickPlayer;
import fr.eyzox.forgefaction.command.LeaveFaction;
import fr.eyzox.forgefaction.proxy.CommonProxy;

@Mod(modid = ForgeFactionMod.MODID, version = ForgeFactionMod.VERSION)
public class ForgeFactionMod
{
	public static final String MODID = "forgefaction";
	public static final String VERSION = "1.0";

	private static ForgeFactionMod INSTANCE;

	public ForgeFactionMod() {
		INSTANCE = this;
	}

	@SidedProxy(clientSide = "fr.eyzox.forgefaction.proxy.ClientProxy", serverSide = "fr.eyzox.forgefaction.proxy.CommonProxy")
	private static CommonProxy proxy;

	private final SimpleNetworkWrapper channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

	private void registerChannel() {
		//channel.registerMessage(MigrantPacket.MigrantPacketHandler.class, MigrantPacket.class,0, Side.SERVER);
		//channel.registerMessage(OpenGUIMigrantPacket.OpenGUIMigrantHandler.class, OpenGUIMigrantPacket.class, 1, Side.CLIENT);
	}

	private void registerCommand() {
		ServerCommandManager m = (ServerCommandManager) MinecraftServer.getServer().getCommandManager();
		m.registerCommand(new CreateFaction());
		m.registerCommand(new InvitePlayer());
		m.registerCommand(new LeaveFaction());
		m.registerCommand(new KickPlayer());
	}

	public static ForgeFactionMod getInstance() { return INSTANCE;}

	public CommonProxy getProxy() {
		return proxy;
	}

	public SimpleNetworkWrapper getChannel() {
		return channel;
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		ModBlocks.registerBlock();

		proxy.registerEvent();
		registerChannel();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		registerCommand();
	}
}
