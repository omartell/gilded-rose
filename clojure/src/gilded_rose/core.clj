(ns gilded-rose.core)

(defn update-quality [items]
  (map
   (fn[item] (cond
               (and (< (:sell-in item) 0) (= "Backstage passes to a TAFKAL80ETC concert" (:name item)))
               (merge item {:quality 0}) ;; Drop quality to zero after concert

               (or (= (:name item) "Aged Brie") (= (:name item) "Backstage passes to a TAFKAL80ETC concert"))
               (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert")
                        (>= (:sell-in item) 5) (< (:sell-in item) 10))
                 ;; Increase backstage passes quality by 2 when there are 10 days or less
                 (merge item {:quality (inc (inc (:quality item)))})

                 (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert")
                          (>= (:sell-in item) 0) (< (:sell-in item) 5))
                   ;; Increase backstage passes quality by 3 when there are 5 days or less
                   (merge item {:quality (inc (inc (inc (:quality item))))})
                   (if (< (:quality item) 50)
                     ;; Otherwise increase quality by 1 for Aged Brie and Backstage passes
                     (merge item {:quality (inc (:quality item))})
                     item)))

               (< (:sell-in item) 0)
               (if (= "Backstage passes to a TAFKAL80ETC concert" (:name item))
                 (merge item {:quality 0}) ;; Again drop quality to zero after concert
                 (if (or (= "+5 Dexterity Vest" (:name item))
                         (= "Elixir of the Mongoose" (:name item)))
                   ;; Once the sell in date has passed degrade quality twice as fast
                   (merge item {:quality (- (:quality item) 2)})
                   item))

               (or (= "+5 Dexterity Vest" (:name item))
                   (= "Elixir of the Mongoose" (:name item)))
               ;; Degrade quality by 1
               (merge item {:quality (dec (:quality item))})

               :else item))
   (map (fn [item]
          (if (not= "Sulfuras, Hand of Ragnaros" (:name item))
            (merge item {:sell-in (dec (:sell-in item))}) ;; Decrement sell-in for all items
            item))
        items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory []
  (let [inventory [(item "+5 Dexterity Vest" 10 20)
                   (item "Aged Brie" 2 0)
                   (item "Elixir of the Mongoose" 5 7)
                   (item "Sulfuras, Hand Of Ragnaros" 0 80)
                   (item "Backstage passes to a TAFKAL80ETC concert" 15 20)]]
    (update-quality inventory)))
