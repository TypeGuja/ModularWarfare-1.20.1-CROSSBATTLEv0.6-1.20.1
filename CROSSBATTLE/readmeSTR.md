src/main/
├── java/nazzy/battle/
│   ├── Battle.java
│   ├── BattleVariables.java
│   ├── ClientProxyBattle.java
│   ├── ServerProxyBattle.java
│   ├── IProxyBattle.java
│   ├── init/
│   │   ├── InitBlocks.java
│   │   ├── InitItems.java
│   │   ├── InitBlockEntities.java
│   │   ├── InitSounds.java
│   │   └── InitMenus.java
│   ├── block/
│   │   ├── BlockClaymore.java
│   │   ├── BlockSandbag.java
│   │   ├── BlockSHOP.java
│   │   ├── BlockTimerBlock.java
│   │   ├── BlockOreCoal.java
│   │   ├── BlockOreIron.java
│   │   ├── BlockOreGold.java
│   │   ├── BlockOreDiamond.java
│   │   ├── BlockDestroyedCoal.java
│   │   ├── BlockDestroyedIron.java
│   │   ├── BlockDestroyedGold.java
│   │   ├── BlockDestroyedDiamond.java
│   │   └── entity/
│   │       ├── ShopBlockEntity.java
│   │       └── DestroyedOreBlockEntity.java
│   ├── item/
│   │   ├── ItemMedkit.java
│   │   ├── ItemKnife.java
│   │   ├── ItemLedorub.java
│   │   └── KnifeProjectile.java
│   ├── gui/
│   │   ├── GuiSHOPGui1.java
│   │   ├── GuiSHOP_Gui_Buy.java
│   │   ├── GuiSHOP_Gui_Buy2.java
│   │   ├── GuiSHOP_Gui_Buy3.java
│   │   ├── GuiSHOP_Gui_Sell.java
│   │   └── GuiContainerMod.java (базовый, если нужен)
│   ├── procedure/
│   │   ├── ProcedureBuy9mmAmmo.java
│   │   ├── ProcedureBuy9mmMag.java
│   │   ├── ProcedureBuy556Ammo.java
│   │   ├── ProcedureBuyClaymore.java
│   │   ├── ProcedureBuyKnife.java
│   │   ├── ProcedureBuyM4.java
│   │   ├── ProcedureBuyM4Mag.java
│   │   ├── ProcedureBuyMedkit.java
│   │   ├── ProcedureBuyMP9.java
│   │   ├── ProcedureBuyP250.java
│   │   ├── ProcedureBuyRifle.java
│   │   ├── ProcedureBuySandbags.java
│   │   ├── ProcedureClaymoreExplode.java
│   │   ├── ProcedureDestroyedCoalTick.java
│   │   ├── ProcedureDestroyedIronTick.java
│   │   ├── ProcedureDestroyedGoldTick.java
│   │   ├── ProcedureDestroyedDiamondTick.java
│   │   ├── ProcedureMedkitUsed.java
│   │   ├── ProcedureOreCoalDestroyed.java
│   │   ├── ProcedureOreIronDestroyed.java
│   │   ├── ProcedureOreGoldDestroyed.java
│   │   ├── ProcedureOreDiamondDestroyed.java
│   │   ├── ProcedurePlayerDies.java
│   │   ├── ProcedurePlayerjoin.java
│   │   ├── ProcedurePlayerRespawns.java
│   │   ├── ProcedurePlayerTick.java
│   │   ├── ProcedureSHOP_gui_sellGUI.java
│   │   ├── ProcedureSHOP_guiSELLALL.java
│   │   ├── ProcedureSHOPgui.java
│   │   ├── ProcedureShopGui_buy1.java
│   │   ├── ProcedureShopGui_buy2.java
│   │   ├── ProcedureShopGui_buy3.java
│   │   ├── ProcedureShopGui_close.java
│   │   ├── ProcedureTimerAdd.java
│   │   ├── ProcedureTimerTick.java
│   │   ├── ProcedureTimerBlockBlockDestroyedByPlayer.java
│   │   └── ProcedureTimerBlockOnBlockRightClicked.java
│   ├── creativetab/
│   │   └── TabTab.java
│   └── overlay/
│       └── OverlayStartgame.java
├── resources/
│   ├── assets/battle/
│   │   ├── blockstates/
│   │   │   ├── claymore.json
│   │   │   ├── sandbag.json
│   │   │   ├── shop.json
│   │   │   ├── timerblock.json
│   │   │   ├── orecoal.json
│   │   │   ├── oreiron.json
│   │   │   ├── oregold.json
│   │   │   ├── orediamond.json
│   │   │   ├── destroyedcoal.json
│   │   │   ├── destroyediron.json
│   │   │   ├── destroyedgold.json
│   │   │   └── destroyeddiamond.json
│   │   ├── models/
│   │   │   ├── block/
│   │   │   │   ├── claymore.json
│   │   │   │   ├── sandbag.json
│   │   │   │   ├── shop.json
│   │   │   │   └── timerblock.json
│   │   │   ├── item/
│   │   │   │   ├── claymore.json
│   │   │   │   ├── destroyedcoal.json
│   │   │   │   ├── destroyeddiamond.json
│   │   │   │   ├── destroyedgold.json
│   │   │   │   ├── destroyediron.json
│   │   │   │   ├── knife.json
│   │   │   │   ├── ledorub.json
│   │   │   │   ├── medkit.json
│   │   │   │   ├── orecoal.json
│   │   │   │   ├── orediamond.json
│   │   │   │   ├── oregold.json
│   │   │   │   ├── oreiron.json
│   │   │   │   ├── sandbag.json
│   │   │   │   ├── shop.json
│   │   │   │   └── timerblock.json
│   │   │   └── custom/
│   │   │       ├── barricade.json
│   │   │       ├── claymore.json
│   │   │       ├── coal_ore.json
│   │   │       ├── destroyed_coal.json
│   │   │       ├── destroyed_diamond.json
│   │   │       ├── destroyed_gold.json
│   │   │       ├── destroyed_iron.json
│   │   │       ├── diamond_ore.json
│   │   │       ├── gold_ore.json
│   │   │       ├── iron_ore.json
│   │   │       ├── knife.json
│   │   │       ├── medkit.json
│   │   │       ├── pickaxe.json
│   │   │       └── shop.json
│   │   ├── textures/
│   │   │   ├── blocks/
│   │   │   │   ├── barricade.png
│   │   │   │   ├── claymore.png
│   │   │   │   ├── coal_ore.png
│   │   │   │   ├── diamond_ore.png
│   │   │   │   ├── gold_ore.png
│   │   │   │   ├── iron_ore.png
│   │   │   │   ├── knife.png
│   │   │   │   ├── medkit.png
│   │   │   │   ├── pickaxe.png
│   │   │   │   └── shop.png
│   │   │   └── gui/
│   │   │       ├── currency_text.png
│   │   │       ├── shop_texture.png
│   │   │       ├── vignette.png
│   │   │       ├── but_text.png
│   │   │       ├── sell_text.png
│   │   │       ├── buy_texture.png
│   │   │       ├── shop_buy1.png
│   │   │       ├── buy_2.png
│   │   │       ├── buy_3.png
│   │   │       ├── ores_texture.png
│   │   │       └── sell_texture.png
│   │   ├── lang/
│   │   │   ├── en_us.json
│   │   │   └── ru_ru.json
│   │   └── sounds.json
│   └── META-INF/
│       └── mods.toml