(ns gilded-rose.core-test
  (:require [gilded-rose.core :refer :all]
            [clojure.test :refer :all]))

(def initial-inventory [(item "+5 Dexterity Vest" 10 20)
                        (item "Aged Brie" 2 0)
                        (item "Elixir of the Mongoose" 5 7)
                        (item "Sulfuras, Hand Of Ragnaros" 0 80)
                        (item "Backstage passes to a TAFKAL80ETC concert" 15 20)])

(defn inventory-after-days [days initial-inventory]
  (reduce (fn [inventory f] (f inventory))
          initial-inventory
          (repeat days update-quality)))

(deftest update-inventory-according-to-rules
  (is (= [{:name "+5 Dexterity Vest", :sell-in 9, :quality 19}
          {:name "Aged Brie", :sell-in 1, :quality 1}
          {:name "Elixir of the Mongoose", :sell-in 4, :quality 6}
          {:name "Sulfuras, Hand Of Ragnaros", :sell-in -1, :quality 80}
          {:name "Backstage passes to a TAFKAL80ETC concert", :sell-in 14, :quality 21}]
         (inventory-after-days 1 initial-inventory)))

  (is (= [{:name "+5 Dexterity Vest", :sell-in -6, :quality -2}
          {:name "Aged Brie", :sell-in -14, :quality 16}
          {:name "Elixir of the Mongoose", :sell-in -11, :quality -20}
          {:name "Sulfuras, Hand Of Ragnaros", :sell-in -16, :quality 80}
          {:name "Backstage passes to a TAFKAL80ETC concert", :sell-in -1, :quality 0}]
         (inventory-after-days 16 initial-inventory))))