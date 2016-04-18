package com.katzstudio.kreativity.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Model for a list of size policies.
 */
public class KrSizePolicyModel {

    private final List<KrUnifiedSize> sizePolicies = new ArrayList<>();

    public KrSizePolicyModel(int count) {
        for (int i = 0; i < count; ++i) {
            sizePolicies.add(new KrUnifiedSize(0, 1));
        }
    }

    public KrSizePolicyModel(KrUnifiedSize... policies) {
        this.sizePolicies.addAll(Arrays.asList(policies));
    }

    public KrSizePolicyModel(List<KrUnifiedSize> sizePolicies) {
        this.sizePolicies.addAll(sizePolicies);
    }

    public int getCount() {
        return sizePolicies.size();
    }

    public Stream<KrUnifiedSize> stream() {
        return sizePolicies.stream();
    }

    public List<Float> getSizes(float availableSpace) {
        float absoluteRequested = stream()
                .map(KrUnifiedSize::getAbsolute)
                .map(KrSizePolicyModel::nonNegative)
                .reduce(0.0f, (a, b) -> a + b);

        float relativeRequested = stream()
                .map(KrUnifiedSize::getRelative)
                .map(KrSizePolicyModel::nonNegative)
                .reduce(0.0f, Float::sum);

        final float totalSpaceAvailable = (availableSpace < absoluteRequested) ? (relativeRequested > 0 ? absoluteRequested * 2 : absoluteRequested) : availableSpace;
        float totalRelativeAvailable = totalSpaceAvailable - absoluteRequested;
        final float relativeUnit = relativeRequested != 0 ? totalRelativeAvailable / relativeRequested : 0;

        return stream()
                .map(size -> (nonNegative(size.getAbsolute()) + nonNegative(size.getRelative()) * relativeUnit) * availableSpace / totalSpaceAvailable)
                .collect(Collectors.toList());
    }

    public List<Integer> getIntSizes(float availableSpace) {
        List<Float> accurateSizes = getSizes(availableSpace);

        int totalUsedSize = 0;
        for (int i = 0; i < accurateSizes.size(); ++i) {
            double value = accurateSizes.get(i);
            int floor = (int) Math.floor(value);
            totalUsedSize += floor;
            accurateSizes.set(i, (float) floor);
        }
        int lastElementIndex = accurateSizes.size() - 1;
        accurateSizes.set(lastElementIndex, accurateSizes.get(lastElementIndex) + availableSpace - totalUsedSize);
        return accurateSizes.stream().map(Float::intValue).collect(Collectors.toList());
    }

    private static float nonNegative(float value) {
        return Math.max(value, 0);
    }
}
