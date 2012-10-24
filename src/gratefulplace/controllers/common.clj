(ns gratefulplace.controllers.common)

;; validation: combination of field name and validation checks
;;
;; validation check group: first member is an error message, the rest
;; of the members are validation checks. If any validation check
;; returns false then the error message is added to a list of error
;; messages for the given field.
;;
;; validation check: a function to apply to the value corresponding to
;; the field name specified in the validation

(defmacro if-valid
  [to-validate validations errors-name & then-else]
  `(let [to-validate# ~to-validate
         validations# ~validations
         ~errors-name (validate to-validate# validations#)]
     (if (empty? ~errors-name)
       ~(first then-else)
       ~(second then-else))))

;; TODO would it be better to use map and filter?
(defn error-messages-for
  "return a vector of error messages or nil if no errors."
  [value validation-check-groups]
  (let [apply-check-group (fn [checks] (reduce #(and % (%2 value)) true checks))]
    (loop [messages [] check-groups validation-check-groups]
      (if-let [check-group (first check-groups)]
        ;; TODO refactor this pattern
        (if (apply-check-group (rest check-group))
          (recur messages (rest check-groups))
          (recur (conj messages (first check-group)) (rest check-groups)))
        messages))))

(defn validate
  "returns a map of errors"
  [to-validate validations]
  (loop [errors {} v validations]
    (if-let [validation (first v)]
      (let [[fieldname & validation-check-groups] validation
            value (get to-validate fieldname)
            error-messages (error-messages-for value validation-check-groups)
            ]
        (if (empty? error-messages)
          (recur errors (rest v))
          (recur (assoc errors fieldname error-messages) (rest v))))
      errors)))