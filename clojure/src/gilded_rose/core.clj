(ns gilded-rose.core)

(def sulfuras "Sulfuras, Hand Of Ragnaros")
(def backstage-passes "Backstage passes to a TAFKAL80ETC concert")
(def brie "Aged Brie")
(def elixir "Elixir of the Mongoose")
(def dexterity "+5 Dexterity Vest")
(def conjured "Conjured")

(defn- expired? [item]
  (< (:sell-in item) 0))

(defn- default-degradation
  "Degradation levels used by normal items"
  [item]
  (if (expired? item) -2 -1))

(defmulti update-item-quality
  "Multimethod used to dispatch the corresponding update-item-quality
  function based on the name attribute of the item"
  :name)

(defmethod update-item-quality :default [item]
  (assoc item :quality (+ (:quality item)
                          (default-degradation item))))

(defmethod update-item-quality sulfuras [item]
  (assoc item :skip-controls true))

(defmethod update-item-quality backstage-passes [item]
  (assoc item :quality (cond
                         (expired? item)           0
                         (<= 5 (:sell-in item) 9)  (+ (:quality item) 2)
                         (<= 0 (:sell-in item) 4)  (+ (:quality item) 3)
                         :else (inc (:quality item)))))

(defmethod update-item-quality brie [item]
  (assoc item :quality (inc (:quality item))))

(defmethod update-item-quality conjured [item]
  (assoc item :quality (+ (:quality item)
                          (* 2 (default-degradation item)))))

(defn- apply-quality-controls
  "Given an item with an updated quality, this makes sure
   the quality of an item is never negative and more than 50.
   If quality controls don't apply to that item, then the
   key :skip-controls must be present."
  [item]
  (if (:skip-controls item)
    (dissoc item :skip-controls)
    (update item :quality #(Math/max 0 (Math/min 50 %)))))

(defn update-quality [items]
  "Given an inventory of items, it returns a new inventory with
   their quality and sell-in value updated according to their
   quality rules."
  (->> items
       (map #(update % :sell-in dec))
       (map update-item-quality)
       (map apply-quality-controls)))

(defn item [item-name, sell-in, quality]
  {:name item-name :sell-in sell-in :quality quality})

(defn update-current-inventory []
  (let [inventory [(item dexterity 10 20)
                   (item brie 2 0)
                   (item elixir 5 7)
                   (item sulfuras 0 80)
                   (item backstage-passes 15) 20]]
    (update-quality inventory)))
