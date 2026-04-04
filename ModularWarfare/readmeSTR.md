src/main/java/com/modularwarfare/
├── ModularWarfare.java (главный класс)
├── api/
│   ├── AnimationUtils.java
│   ├── MWArmorType.java
│   ├── WeaponAnimation.java
│   ├── WeaponAnimations.java
│   ├── WeaponEvent.java
│   ├── WeaponFireEvent.java
│   └── WeaponReloadEvent.java
├── client/
│   ├── ClientProxy.java
│   ├── ClientRenderHooks.java
│   ├── ClientTickHandler.java
│   ├── anim/
│   │   ├── AnimStateMachine.java
│   │   ├── ReloadType.java
│   │   ├── StateEntry.java
│   │   └── StateType.java
│   ├── config/
│   │   ├── AmmoRenderConfig.java
│   │   ├── ArmorRenderConfig.java
│   │   ├── AttachmentRenderConfig.java
│   │   ├── BackpackRenderConfig.java
│   │   ├── BackpackRenderConfig.java
│   │   └── GunRenderConfig.java
│   ├── gui/
│   │   ├── GuiInventoryModified.java
│   │   └── GuiPlayerInventory.java
│   ├── handler/
│   │   ├── KeyInputHandler.java
│   │   ├── RenderGuiHandler.java
│   │   └── ClientWeaponHandler.java
│   ├── hud/
│   │   ├── AttachmentUI.java
│   │   └── GunUI.java
│   ├── input/
│   │   ├── KeyBindingDisable.java
│   │   ├── KeyBindingEnable.java
│   │   ├── KeyEntry.java
│   │   └── KeyType.java
│   ├── model/
│   │   ├── ModelAmmo.java
│   │   ├── ModelAttachment.java
│   │   ├── ModelBackpack.java
│   │   ├── ModelBullet.java
│   │   ├── ModelCustomArmor.java
│   │   ├── ModelGun.java
│   │   ├── animations/
│   │   │   ├── AnimationCustom.java
│   │   │   ├── AnimationPistol.java
│   │   │   ├── AnimationRifle.java
│   │   │   ├── AnimationRifle2.java
│   │   │   ├── AnimationRifle3.java
│   │   │   ├── AnimationRifle4.java
│   │   │   ├── AnimationShotgun.java
│   │   │   ├── AnimationSideClip.java
│   │   │   ├── AnimationSniperBottom.java
│   │   │   ├── AnimationSniperTop.java
│   │   │   └── AnimationTopRifle.java
│   │   ├── layers/
│   │   │   ├── RenderLayerBackpack.java
│   │   │   └── RenderLayerBody.java
│   │   ├── objects/
│   │   │   ├── BreakActionData.java
│   │   │   ├── CustomItemRenderType.java
│   │   │   ├── CustomItemRenderer.java
│   │   │   └── RenderVariables.java
│   │   └── renders/
│   │       ├── InstantBulletRenderer.java
│   │       ├── RenderAmmo.java
│   │       ├── RenderArms.java
│   │       ├── RenderAttachment.java
│   │       ├── RenderDecal.java
│   │       ├── RenderGunStatic.java
│   │       ├── RenderParameters.java
│   │       └── RenderShell.java
│   ├── export/
│   │   └── ItemModelExport.java
│   └── scope/
│       ├── ScopeRenderGlobal.java
│       └── ScopeUtils.java
├── common/
│   ├── CommonProxy.java
│   ├── MWTab.java
│   ├── armor/
│   │   ├── ArmorType.java
│   │   ├── ItemMWArmor.java
│   │   └── ItemSpecialArmor.java
│   ├── backpacks/
│   │   ├── BackpackType.java
│   │   └── ItemBackpack.java
│   ├── capability/
│   │   ├── CapabilityExtra.java
│   │   ├── ExtraContainer.java
│   │   ├── ExtraContainerProvider.java
│   │   └── IExtraItemHandler.java
│   ├── container/
│   │   ├── ContainerInventoryModified.java
│   │   ├── SlotBackpack.java
│   │   └── SlotVest.java
│   ├── entity/
│   │   ├── debug/
│   │   │   ├── EntityDebugAABB.java
│   │   │   ├── EntityDebugColor.java
│   │   │   ├── EntityDebugDot.java
│   │   │   ├── EntityDebugVector.java
│   │   │   ├── RenderDebugAABB.java
│   │   │   ├── RenderDebugDot.java
│   │   │   └── RenderDebugVector.java
│   │   └── decals/
│   │       ├── EntityBulletHole.java
│   │       ├── EntityDecal.java
│   │       └── EntityShell.java
│   ├── extra/
│   │   └── ItemLight.java
│   ├── guns/
│   │   ├── AmmoType.java
│   │   ├── AttachmentEnum.java
│   │   ├── AttachmentType.java
│   │   ├── BulletProperty.java
│   │   ├── BulletType.java
│   │   ├── GunType.java
│   │   ├── ItemAmmo.java
│   │   ├── ItemAttachment.java
│   │   ├── ItemBullet.java
│   │   ├── ItemGun.java
│   │   ├── ItemSpray.java
│   │   ├── MWDamageSources.java
│   │   ├── PotionEffectEnum.java
│   │   ├── PotionEntry.java
│   │   ├── SkinType.java
│   │   ├── SprayType.java
│   │   ├── WeaponDotColorType.java
│   │   ├── WeaponFireMode.java
│   │   ├── WeaponScopeType.java
│   │   └── WeaponSoundType.java
│   ├── handler/
│   │   ├── EventHandlerEntity.java
│   │   ├── GuiHandler.java
│   │   └── ServerTickHandler.java
│   ├── hitbox/
│   │   ├── PlayerHitbox.java
│   │   ├── PlayerSnapshot.java
│   │   ├── hits/
│   │   │   ├── BulletHit.java
│   │   │   └── PlayerHit.java
│   │   ├── maths/
│   │   │   ├── EnumHitboxType.java
│   │   │   └── RotatedAxes.java
│   │   └── playerdata/
│   │       ├── PlayerData.java
│   │       └── PlayerDataHandler.java
│   ├── network/
│   │   ├── NetworkHandler.java
│   │   ├── PacketAimingReponse.java
│   │   ├── PacketAimingRequest.java
│   │   ├── PacketBase.java
│   │   ├── PacketBulletSnap.java
│   │   ├── PacketClientAnimation.java
│   │   ├── PacketDecal.java
│   │   ├── PacketGunAddAttachment.java
│   │   ├── PacketGunFire.java
│   │   ├── PacketGunReload.java
│   │   ├── PacketGunReloadSound.java
│   │   ├── PacketGunSwitchMode.java
│   │   ├── PacketGunTrail.java
│   │   ├── PacketGunUnloadAttachment.java
│   │   ├── PacketOpenExtraArmorInventory.java
│   │   ├── PacketOpenGui.java
│   │   ├── PacketOpenNormalInventory.java
│   │   ├── PacketPlayHitmarker.java
│   │   ├── PacketPlaySound.java
│   │   ├── PacketSyncBackWeapons.java
│   │   └── PacketSyncExtraSlot.java
│   ├── particle/
│   │   └── EntityBloodFX.java
│   ├── protector/
│   │   └── ModularProtector.java
│   ├── type/
│   │   ├── BaseItem.java
│   │   ├── BaseType.java
│   │   ├── ContentTypes.java
│   │   └── TypeEntry.java
│   └── vector/
│       ├── Matrix.java
│       ├── Matrix3f.java
│       ├── Matrix4f.java
│       ├── ReadableVector.java
│       ├── ReadableVector2f.java
│       ├── ReadableVector3f.java
│       ├── ReadableVector4f.java
│       ├── Vector.java
│       ├── Vector2f.java
│       ├── Vector3f.java
│       ├── Vector3i.java
│       ├── Vector4f.java
│       ├── WritableVector2f.java
│       ├── WritableVector3f.java
│       └── WritableVector4f.java
├── loader/
│   ├── MWModelBase.java
│   ├── MWModelBipedBase.java
│   ├── ObjModel.java
│   ├── ObjModelBuilder.java
│   ├── api/
│   │   ├── AbstractObjModel.java
│   │   └── ObjModelRenderer.java
│   ├── part/
│   │   ├── Face.java
│   │   ├── ModelObject.java
│   │   ├── TextureCoordinate.java
│   │   └── Vertex.java
│   └── ModelFormatException.java
├── objects/
│   └── SoundEntry.java
└── utility/
├── ColorUtils.java
├── DevGui.java
├── ForgeEvent.java
├── GSONUtils.java
├── ModConfig.java
├── ModUtil.java
├── MWSound.java
├── NonDumbAxisABB.java
├── NumberHelper.java
├── RayUtil.java
├── RenderHelperMW.java
└── event/
└── ForgeEvent.java