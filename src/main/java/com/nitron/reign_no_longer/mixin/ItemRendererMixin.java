package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useFatesSeveranceModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ReignNoLongerItems.FATES_SEVERANCE) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.GROUND) {
            return ((ItemRendererAccessor) this).reignNoLonger$getModels().getModelManager().getModel(new ModelIdentifier(ReignNoLonger.MOD_ID, "fates_severance_handheld", "inventory"));
        }
        return value;
    }

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useShatteredSeveranceModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ReignNoLongerItems.SHATTERED_SEVERANCE) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.GROUND) {
            return ((ItemRendererAccessor) this).reignNoLonger$getModels().getModelManager().getModel(new ModelIdentifier(ReignNoLonger.MOD_ID, "shattered_severance_handheld", "inventory"));
        }
        return value;
    }

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel useNullibladeModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ReignNoLongerItems.NULLIBLADE) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.GROUND) {
            return ((ItemRendererAccessor) this).reignNoLonger$getModels().getModelManager().getModel(new ModelIdentifier(ReignNoLonger.MOD_ID, "nulliblade_handheld", "inventory"));
        }
        return value;
    }

    @ModifyVariable(method = "renderItem", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel usePureBalanceModel(BakedModel value, ItemStack stack, ModelTransformationMode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (stack.isOf(ReignNoLongerItems.PURE_BALANCE) && renderMode != ModelTransformationMode.GUI && renderMode != ModelTransformationMode.GROUND) {
            return ((ItemRendererAccessor) this).reignNoLonger$getModels().getModelManager().getModel(new ModelIdentifier(ReignNoLonger.MOD_ID, "pure_balance_handheld", "inventory"));
        }
        return value;
    }
}