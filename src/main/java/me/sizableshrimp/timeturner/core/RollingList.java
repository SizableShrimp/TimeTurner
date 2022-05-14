package me.sizableshrimp.timeturner.core;

import com.google.common.collect.ForwardingList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

public final class RollingList<E> extends ForwardingList<E> {
    private final List<E> delegate;
    private final int maxSize;
    private int insertIdx = 0;

    public RollingList(int maxSize) {
        checkArgument(maxSize >= 0, "maxSize (%s) must >= 0", maxSize);
        this.delegate = new ArrayList<>(maxSize);
        this.maxSize = maxSize;
    }

    public RollingList(RollingList<? extends E> c) {
        this(c.maxSize);
        this.insertIdx = c.insertIdx;
        addAll(c);
    }

    @NotNull
    @Override
    protected List<E> delegate() {
        return this.delegate;
    }

    private void ensureCapacity(int added) {
        if (this.delegate.size() + added > maxSize)
            throw new UnsupportedOperationException("Cannot increase size of rolling list past maxSize of " + this.maxSize);
    }

    @Nullable
    public E getWrapped(int index) {
        return this.delegate.isEmpty() ? null : this.delegate.get(index % this.delegate.size());
    }

    @Nullable
    public E getHead() {
        return this.getWrapped(this.insertIdx);
    }

    @Nullable
    public E getTail() {
        return this.getWrapped(this.insertIdx - 1);
    }

    @Nullable
    public E getTailOffset(int offset) {
        return this.getWrapped(this.insertIdx - 1 + offset);
    }

    @Nullable
    public E getTailOffsetPositive(int offset) {
        return this.getWrapped(Math.max(0, this.insertIdx - 1 + offset));
    }

    public int getInsertionIndex() {
        return this.insertIdx;
    }

    public int setInsertionIndex(int insertIdx) {
        int oldIdx = this.insertIdx;
        this.insertIdx = insertIdx;
        return oldIdx;
    }

    public int moveInsertionIndex(int offset) {
        int oldIdx = this.insertIdx;
        this.insertIdx += offset;
        return oldIdx;
    }

    @Override
    public void add(int index, E element) {
        ensureCapacity(1);

        // TODO increment insertIdx here depending on if index is before or after?
        this.delegate.add(index, element);
    }

    @Override
    public boolean add(E e) {
        int insertIdx = this.insertIdx++;
        if (this.delegate.size() < this.maxSize)
            return delegate.add(e);

        delegate.set(insertIdx % this.maxSize, e);
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> c) {
        ensureCapacity(c.size());

        return this.delegate.addAll(index, c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        ensureCapacity(c.size());

        return this.delegate.addAll(c);
    }
}
