(ns gilded-rose.core-test
  (:require [gilded-rose.core :refer :all]
            [clojure.test :refer :all]))

(def initial-inventory [(item dexterity 10 20)
                        (item brie 2 0)
                        (item elixir 5 7)
                        (item sulfuras 0 80)
                        (item backstage-passes 15 20)])

(defn inventory-after-days [days initial-inventory]
  (reduce (fn [inventory f] (f inventory))
          initial-inventory
          (repeat days update-quality)))

(deftest update-inventory-according-to-rules
  (is (= [{:name dexterity, :sell-in 9, :quality 19}
          {:name brie, :sell-in 1, :quality 1}
          {:name elixir, :sell-in 4, :quality 6}
          {:name sulfuras, :sell-in -1, :quality 80}
          {:name backstage-passes, :sell-in 14, :quality 21}]
         (inventory-after-days 1 initial-inventory)))

  (is (= [{:name dexterity, :sell-in -6, :quality 0}
          {:name brie, :sell-in -14, :quality 16}
          {:name elixir, :sell-in -11, :quality 0}
          {:name sulfuras, :sell-in -16, :quality 80}
          {:name backstage-passes, :sell-in -1, :quality 0}]
         (inventory-after-days 16 initial-inventory))))

(deftest conjured-items-degrade-quality-twice-as-fast
  (testing "quality decreases by 2 before sell-by date"
    (is (= [{:name conjured :sell-in 0 :quality 0}]
           (inventory-after-days 5 [(item "Conjured" 5 10)]))))

  (testing "quality decreases by 4 once sell-by date has passed"
    (is (= [{:name conjured :sell-in -2 :quality 2}]
           (inventory-after-days 7 [(item "Conjured" 5 20)]))))

  (testing "quality is never be negative once sell by date has passed"
    (is (= [{:name conjured :sell-in -5 :quality 0}]
           (inventory-after-days 5 [(item "Conjured" 0 0)])))))

(deftest quality-is-never-more-than-50
  (is (= [{:name brie :sell-in 0 :quality 50}]
         (update-quality [{:name brie :sell-in 1 :quality 50}]))))
