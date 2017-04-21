package com.handstandsam.maintainableespresso.models;


public class ItemBuilder {

    Item item;

    public ItemBuilder(String label) {
        item = new Item(label);
    }

    public ItemBuilder label(String label) {
        item.label = label;
        return this;
    }

    public Item build() {
        return item;
    }

    public ItemBuilder image(String image) {
        item.image=image;
        return this;
    }
}
