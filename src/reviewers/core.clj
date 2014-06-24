(ns reviewers.core
 (:use
   hiccup.core)
 (:require [clj-time.core :as t]
           [clj-time.format :as f]
           [clj-time.predicates :as pr]))

(def pool2
  '(
    "Person A"
    "Person B"
    "Person C"
))

(def pool1
  '(
  "Person 1"
  "Person 2"
  "Person 3"
  "Person 4"
  "Person 5"
))

(def start-date (t/date-time 2014 06 20))

(defn fut [date]
  (t/plus date (t/days 1)))

(defn days
  ([] (days start-date))
  ([d] (cons d (lazy-seq (days (fut d))))))

(defn weekdays []
  (filter pr/weekday? (days)))

(def reviewers (map vector (cycle pool1) (cycle pool2)))

(defn schedule [n]
  (map vector (take n (weekdays)) (take n reviewers)))
(schedule 2)

(def big-sched (schedule 4000))

(defn today-reviewers []
  (filter (fn [x] (= (first x) (t/today-at 00 00))) big-sched))

(defn list-reviewers [n]
  (take n big-sched))

(defn start-today [coll]
  (filter (fn [x] (or
                    (t/after? (first x) (t/today-at 00 00))
                    (= (first x) (t/today-at 00 00)))) coll))

(def date-formatter (f/formatter "E dd-MM-yyyy"))

(defn format-reviewers [coll]
  (for [x coll] [:p x]))

(defn format-list [coll]
  (html [:title "Reviewers"]
        (for [x coll]
            [:p
               [:h2 (f/unparse date-formatter (first x))] 
               (format-reviewers (first (rest x)))])))


(list-reviewers 1)
(for [x (first (list-reviewers 1))] x)
(start-today (list-reviewers 10))
(format-list (list-reviewers 2))
(format-reviewers '("foo" "bar"))
(format-reviewers (today-reviewers))
