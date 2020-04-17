(ns xyzunited.core
  (:require-macros
   [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require
   [secretary.core :as secretary]
   [goog.events :as events]
   [goog.history.EventType :as EventType]
   [reagent.core :as reagent]
   [datafrisk.core :as d]
   [cljs.reader :refer [read-string]]
   [cljsjs.material-ui]
   [cljs-react-material-ui.core :as ui]
   [cljs-react-material-ui.reagent :as rui]
   [cljs-react-material-ui.icons :as ic]))

(defn js-console 	[data] (.log js/console (str data)))
(defn st        	[a] (read-string a))
(defn big       	[] (rand-int 987654321))
(defn round		[x & {p :precision}]
      (if p (let [scale (Math/pow 10 p)]
          (-> x (* scale) Math/round (/ scale)))
                          (Math/round x)))
(defonce debug? 	^boolean js/goog.DEBUG)
(defn dev-setup 	[] (when debug?
    			(enable-console-print!)
    			(println "dev mode")))
(defonce app-state
  (reagent/atom {:page :nil}))
(def cozmo-db
  (reagent/atom {:name "Validator Portal"
         :view 0
         :alt false
         :counter 0
         :toggle false
         :toggle3D false
         :container nil
         :profile "empty"
         :skills #{}
         :goal1 #{}
         :goal2 "Goal"
         :preview false
        }))
(def active (reagent/atom 0))
(def styles {
:1{:primary"#ff5252":secondary"#414345":third"#FF6E40":sh"65,67,69,":light "#fff"}
:2{:primary"#009688":secondary"#414345":third"#FFF176":sh"65,67,69,":light "#fff"}
:3{:primary"#319113":secondary"#010002":third"#030F01":sh"53,92,125,":light "#FFFFFF"}
:4{:primary"#332532":secondary"#2A363B":third"#F2AE72":sh"42,54,59,":light "#8C4646"}
:5{:primary"#A8A7A7":secondary"#363636":third"#263248":sh"54,54,54,":light "#fff"}
:6{:primary"#303832":secondary"#FF9800":third"#F2E58A":sh"89,79,79,":light "#FFF1B3"}
  })
(def styl
  (reagent/atom (:1 styles)))
(defn resetstyl [i] (reset! styl (i styles)))
(defn muz [content]
  [rui/mui-theme-provider
   {:mui-theme (ui/get-mui-theme {:palette {
	:primary1-color (:primary @styl)
	:primary2-color (:secondary @styl)
	}})} [:div content]])

;; Routes

(defn hook-browser-navigation! [] (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes [] (secretary/set-config! :prefix "#")
  (defroute "/" []
    (swap! app-state assoc :page :home))
  (defroute "/intro" []
    (swap! app-state assoc :page :intro))
  (defroute "/login" []
    (swap! app-state assoc :page :login))
  (hook-browser-navigation!))

;; EDN

(def roles ["Hustler" "Hacker" "Designer" "Sales" "Intern" "Lawyer"
"Financial" "Investor" "Advisor" "HR" "Marketing" "Engineer"])
(def interests ["Startup" "Tech" "Design" "Sales" "School" "Law"
"Finance" "Invest" "Advice" "HR" "Marketing" "Engineering"])
(def goal-list [
"Join the Harmony Community"
"Offer Jobs"
"Browse Projects"
"Validate an Idea"
"Build a Project"
"Join a Project"
"Find a Startup Job"
"Find a Mentor"
"Build more connections with Entrepreneurs"
"Mentoring a Project"])
(def skillmap {
:all [
"Skill""Skill""Skill""Skill""Skill""Skill"
"Skill""Skill""Skill""Skill""Skill""Skill"
]
:Hustler ["Sales""Business""Communication""Engineering""Design""Law"]
:Hacker ["Programming""Engineering""Design""Communication"]
:Designer ["Design""Programming""Communication""Communication"]
:Sales ["Sales""Business""Communication""Law"]
:Intern ["Sales""Business""Communication""Engineering""Design"]
:Lawyer ["Business""Law""Advisory""Communication"]
:Financial ["Business""Law""Sales""Advisory"]
:Investor ["Sales""Business""Communication""Advisory""Law"]
:Advisor ["Sales""Business""Communication""Advisory""Law"]
:HR ["Sales""Business""Communication""HR Skill"]
:Marketing ["Sales""Business""Communication""Design"]
:Engineer ["Business""Engineering""Design""Programming"]})

;; Components

(defn tile-ext [name]
[:div { 
	:style {:background (:light @styl)
	:box-shadow
	(if (= name (:profile @cozmo-db))
	(str "0px 3vmin 6vmax 0.1vmax rgba("(:sh @styl)" .26)")"")}
	:class (str "tile-ext "
	(if (= name (:profile @cozmo-db)) "tile-ext-show" ""))}
	[:div {:class "tile-ext-content"
		:style {:background (:light @styl)}}
		[:h1 name]
		[:p {:style {:padding "20px" :font-size "3vh"}} 
(case name


"Hustler" "\"Focused. I'm a hustler. And my hustle is trying to figure out the best ways to do what I like without having to do much else.\""

"Hacker" "\"At the end of the day, my goal was to be the best hacker.\""

"Designer" "\"A designer knows he has achieved perfection not when there is nothing left to add, but when there is nothing left to take away.\""

"Sales" "\"There's no lotion or potion that will make sales faster and easier for you - unless your potion is hard work.\""

"Intern" "\"To raise new questions, new possibilities, to regard old problems from a new angle, requires creative imagination and marks real advance in science.\""

"Lawyer" "\"A lawyer without history or literature is a mechanic, a mere working mason; if he possesses some knowledge of these, he may venture to call himself an architect.\""

"Financial" "\"Financial institutions, the corporate world and civil society - all must uphold high standards of probity in their working. Only a genuine partnership between the Government and its people can bring about positive change to create a just society.\""

"Investor" "\"When you're an investor, you can look at the quantitative and qualitative elements of an investment, but there's a third aspect: What you feel in your gut.\""

"Advisor" "\"Let no man in the world live in delusion. Without a Guru none can cross over to the other shore.\""

"HR" "\"As an HR Manager, you don't have to build a top-down perks program. You decide how much you want to invest in your employees, and then you give your employees the control to build a custom perk package for themselves.\""

"Marketing" "\"Advertisers and marketers should be looking to bring new experiences to different parts of the brain. It's a more profound idea than just dropping a billboard into a video game.\""

"Engineer" "\"As an engineer I'm constantly spotting problems and plotting how to solve them.\""


)
]]
	[:img {:src (str "icons/" name ".svg") :alt name}]
	[:button {
		:style {:background (:light @styl)}
		:on-click #(swap! active inc)}"Elfogad"]])

(defn tile [name] 
[:div {	:style {:width
   (if (= "empty" (:profile @cozmo-db)) "22vw !important" "")
:box-shadow (str "0px 3vmin 6vmax 0.1vmax rgba("(:sh @styl)" .26)")}
	:class (str "tile "
   (if (= name (:profile @cozmo-db)) "tile-selected" "")

)}
	[:a {:class "tile__inner" :on-click 
	#(do
(swap! cozmo-db assoc :profile name)
(case name
"Hustler" (resetstyl :1)
"Hacker" (resetstyl :2)
"Designer" (resetstyl :3)
"Sales" (resetstyl :4)
"Intern" (resetstyl :1)
"Lawyer" (resetstyl :2)
"Financial" (resetstyl :3)
"Investor" (resetstyl :4)
"Advisor" (resetstyl :1)
"HR" (resetstyl :2)
"Marketing" (resetstyl :3)
"Engineer" (resetstyl :4)
)
)
	:style {:color (:secondary @styl)}}
	[:img {:src (str "icons/" name ".svg") :alt name}]
	[:p {:class "tile__title"} name]]
	])

(defn tile-wrap [][:div [:div {:class "tile-wrap"
	:style {:background "rgba(255,255,255,0)"}}
	(map tile roles)]
	(map tile-ext roles)])

(defn go-button [] 
[:button {	:class "gobutton"
	:on-click #(if (= @active 5)
			(if (:preview @cozmo-db)
			  (swap! cozmo-db assoc :preview false)
			  (swap! cozmo-db assoc :preview true))
			(swap! active inc))}
(case @active
0 "Let's Go"
1 "nil"
2 "Confirm"
3 "Advance"
4 "Accept"
5 (if (:preview @cozmo-db) "Back" "Preview"))
  [:svg {:viewBox "0 0 150 20"
	 :xmlns "http://www.w3.org/2000/svg"
	 :class "button__arrow"
	 :style {:fill (:secondary @styl)}}
  (if (or
	(= @active 0)
	(= @active 2)
	(= @active 3)
)
[:path {:d "M146.209 9l-4.282-4.288-.701-.702 1.38-1.425.701.702 5.302 5.309.011-.011.685.707.196.196-.003.003.502.519-.701.702-5.992 6-.701.702-1.38-1.425.701-.702 4.282-4.288h-146.209v-2h146.209z"}])
]])

(def firstname (reagent/atom nil))
(def lastname (reagent/atom nil))
(def email (reagent/atom nil))

(defn text-block []
[:div.text-block
[rui/paper {:style
{	:background "rgba(255,255,255,1)"
	:position "absolute"	:padding "4vmin"
	:width "36vw"		:height "38vmin"
	:top "10vmin"		:left
		(if (:preview @cozmo-db)"-100%" "10vmin")
  :-webkit-transition "all 0.7s ease"
  :-moz-transition "all 0.7s ease"
  :-o-transition "all 0.7s ease"
  :transition "all 0.7s ease"
  :transition-timing-function "cubic-bezier(0.68, -0.55, 0.265, 1.55)"}}
	[:div {:style {		:-webkit-transform "skewY(-0deg)"
				:transform "skewY(-0deg)"}} 
	[:div {:style {:float "left" :width "48%" :padding "1%"}}
		[ui/text-field {
			:value @firstname
	:on-change #(reset! firstname (-> % .-target .-value))
	 		:floating-label-text "First Name"
	 		:full-width true}]]
	[:div {:style {:float "left" :width "48%" :padding "1%"}}
		[ui/text-field {
			:value @lastname
	:on-change #(reset! lastname (-> % .-target .-value))
			:floating-label-text "Last Name"
			:full-width true}]]]
	[:div {:style {:float "left" :width "98%" :padding "1%"
			:-webkit-transform "skewY(-0deg)"
			:transform "skewY(-0deg)"}}
		[ui/text-field {
			:value @email
	:on-change #(reset! email (-> % .-target .-value))
			:floating-label-text "Mail"
			:full-width true}]]]
[rui/paper {:style
{	:background "rgba(255,255,255,1)"
	:position "absolute"	:padding "4vmin"
	:width "36vw"		:height "60vmin"
	:top "10vmin"		:right 
		(if (:preview @cozmo-db)"10vmin" "-100%")
  :-webkit-transition "all 0.7s ease 0.7"
  :-moz-transition "all 0.7s ease 0.7"
  :-o-transition "all 0.7s ease 0.7"
  :transition "all 0.7s ease 0.7"
  :transition-timing-function "cubic-bezier(0.68, -0.55, 0.265, 1.55)"
}}
	[:div 
[:style (if (:preview @cozmo-db)
" .cards ul li .card.end::after {
  -webkit-transform: rotateZ(-120deg);
          transform: rotateZ(-120deg);
  height: 100%;
  width:100%;}"
" .cards ul li .card.end::after {
  -webkit-transform: rotateZ(-60deg);
          transform: rotateZ(-60deg);
  height: 100%;
  width:100%;}"
)]
		(str "Name: " @firstname " " @lastname)[:br]
		(str "Email: " @email)[:br]
		(str "Profile: "(:profile @cozmo-db))[:br]
		(str "Goal: " (:goal1 @cozmo-db))[:br]
		(str "Skills: " (:skills @cozmo-db))[:br]
		(str "Interests: " (:interests @cozmo-db))[:br]

	]]
]
)

(defn tags [] [:div
[:h1 {:style {:margin-top "10vmin" :color (:secondary @styl)}}
"I want to" (if (= #{} (:goal1 @cozmo-db))
		".."
		(map #(str " " %
		  (if (= (last (:goal1 @cozmo-db)) %)"."","))
		    (:goal1 @cozmo-db))
	    )]
[:div {:class "sibling-fade"}
	(map (fn [name] 
		[:span {:class (if (contains? (:goal1 @cozmo-db) name)
			"tooltip tagged""tooltip")}
		[:style (str ".tagged {background:"
				(:secondary @styl)"}")]
		[:a {
		:style (if
		(= name (:goal1 @cozmo-db))
		{:background (:secondary @styl)
		 :color (:primary @styl)}
		{:color (:primary @styl)})
		:on-click #(if (contains? (:goal1 @cozmo-db) name)
			(swap! cozmo-db update :goal1 disj name)
			(swap! cozmo-db update :goal1 conj name))}
			name]
			[:span {:class "tooltip__content"}
			[:p (rand-int 99999) " people " name]]])
	goal-list)
][:h2 "(multiple choice!)"]])

(defn cdeg
([deg n](str "rotate(" deg "deg) skew(" (- 90 (/ 360 n)) "deg)"))
([n](str "skew(-"(- 90 (/ 360 n))"deg) rotate(-"(- 90 (/ 180 n))"deg)")))

(defn circ [content n]
(map (fn [name deg]
	[:div {:class "cn-li firstcirc"
		:style {
		:-webkit-transform (cdeg deg n)
		:-moz-transform (cdeg deg n)
		:-ms-transform (cdeg deg n)
		:transform (cdeg deg n)}}
	[:a {
	:on-click #(if (contains? (:skills @cozmo-db) name)
			   (swap! cozmo-db update :skills disj name)
			   (swap! cozmo-db update :skills conj name))
		:style {
		:-webkit-transform (cdeg n)
		:-moz-transform (cdeg n)
		:-ms-transform (cdeg n)
		:transform (cdeg n)
		}}[:span name]]])
	content (map #(* (/ 360 n) %) (range n))))

(defn circ2 [content n]
(map
(fn [name] [:div {:class "cn-li secondcirc"}
		[:a [:span name]]])
	content))


(defn circular-nav [] 
(let [	state	(reagent/atom true)
	default	((st (str ":all")) skillmap)
	cn-comp	(if (:toggle3D @cozmo-db)
			"cn-component floating""cn-component")
	cn-b	(if (:toggle3D @cozmo-db)"cn-button view3d""cn-button")
	]
    (fn []
	[:div {:class cn-comp}
	    [:button {	:class cn-b
			:id "cn-button"
			:on-click
			#(if @state
				(reset! state false)
				(reset! state true))
			:style (if @state {}{})}
	(if @state  (:profile @cozmo-db) "Show Skills")
		]
	    [:div {:class (str "cn-wrapper "
			(if (:toggle3D @cozmo-db) "view3d """)
			(if @state "opened-nav"""))
		   :id "cn-wrapper"}
		[:div {:class "cn-ul"}
		(circ ((st (str ":"(:profile @cozmo-db))) skillmap)
		(count ((st (str ":"(:profile @cozmo-db)))skillmap))
			)
		(circ2 default
		(count ((st (str ":"(:profile @cozmo-db)))skillmap))
			)
		]
	    ]
	])))

(defn preview [] [:div
 [:div {:class "grow" :style {:width "60vw" :left "" :right "0"}}
	[:div {:style {:position "absolute" :top 0 :width "70%"
		:left "15%" :right "15%"}}
;	[:h1 "Profile preview"]
]]
 [:div {:class "imgtxt" :style {:top "30vmin" :right "-15%"}}
	[:h1 "Your future has begun."]
 [:div {:class "author"} "Hover to see the preview"]]])

(defn expandable [name] [:div {:class "grid-card is-collapsed"}
        [:div {:class "card__inner js-expander" :style {
		:color (:primary @styl)
		:background "white"}}
        	[:span name]
        	[:i {:class "fa fa-folder-o"}]]
        [:div {:class "card__expander"}
        	[:i {:class "fa fa-close js-collapser"}]
        "Tartalom"]])
(defn gallery [] 
        [:div {:class "grid-cards"}
	(map expandable interests)
	])

(defn orb []
[:a {:on-click #(if (:toggle @cozmo-db)
		(swap! cozmo-db assoc :toggle false)
		(swap! cozmo-db assoc :toggle true))}
	[ui/avatar {:style {
	:position "fixed":top "4vmin":right "7vmin"
	:height "11vmin":width "11vmin":z-index "3300"
	:cursor "pointer":color (:secondary @styl):font-size "2vmin"
	;:background (:third @styl)
	:background "white"
	:box-shadow "0 1vmin 3vmin .5vmin rgba(0, 0, 0, 0.2)"}}
		"Name"]])
(defn button3d []
(if (:toggle3D @cozmo-db)
[:a {:on-click #(swap! cozmo-db assoc :toggle3D false)}[:div {:class "toggle3D"}
                "Disable 3D"]]
[:a {:on-click #(swap! cozmo-db assoc :toggle3D true)}[:div {:class "toggle3D"}
                "Enable 3D"]]
                ))

(defn search-bar []
[:div {:class "search"}
    [:div {:class "search_bar"}
      [:input {:id "searchOne" :type "checkbox"}]
      [:label {:for "searchOne"}
        [:i {:class "icon ion-android-search"}]
        [:i {:class "last icon ion-android-close"}]
        [:p"|"]
      ]
      [:input {:placeholder "Search..." :type "text"} ]]])

(defn progress-bar []
  (let [progress		(round (* @active (/ 100 6)))]
    [:div.lvldesign [:p.lvlinfo (str "Progress: " progress "%, "
		(if (> 100 progress) "Level 0")
		(if (= 100 progress) "You have reached Level 1!")
		(if (< 100 progress) "Level 1"))]
		[:p.lvlinfo2 
		  (str "Episode: Create Profile / "
			(case @active
				0 "Introduction"
				1 "Role"
				2 "Skill"
				3 "Main Goals"
				4 "Interests Library"
				5 "Go Online"
				))]
[ui/linear-progress {:mode "determinate":value (* @active (/ 100 6))
:style {	:position "fixed" :bottom "2vmin"
		:z-index "3030" :width "36vw" :left "32vw"}}]]))

(defn drawer []
[rui/drawer
	{:width 400 :open-secondary true
	 :open (:toggle @cozmo-db) :style {:z-index "3200"}}
	[rui/paper {:style {	:background (:secondary @styl)
				:height "100vh":color (:third @styl)}}
		(button3d)
		(str "Name: "(:name @cozmo-db))[:br]
		(str "View: "(:view @cozmo-db))[:br]
		(str "Active step: " @active )[:br]
		(str "Page: "(:page @app-state))[:br]
		(str "Profile: "(:profile @cozmo-db))[:br]
		(str "Goal: " (:goal1 @cozmo-db))[:br]
		(str "Skills: " (:skills @cozmo-db))[:br]
		[:br][:a {:on-click #(resetstyl :1)}"Scheme 1"]
		[:br][:a {:on-click #(resetstyl :2)}"Scheme 2"]
		[:br][:a {:on-click #(resetstyl :3)}"Scheme 3"]
		[:br][:a {:on-click #(resetstyl :4)}"Scheme 4"]
		[:br][:a {:on-click #(resetstyl :5)}"Scheme 5"]
		[:br][:a {:on-click #(resetstyl :6)}"Scheme 6"]
	]])
(defn guide-style []
(case (+ @active 1)
	1	".guide {left: 10%; top: 20%;}"
	2	(if (= "empty" (:profile @cozmo-db))
			".guide {left: 60%; top: 20%;}"
			".guide {left: 60%; top: 60%;}"
			)
	3	".guide {left: 10%; top: 75%;}"
	4	".guide {left: 20%; top: calc(100% - 20vh);}"
	5	".guide {left: 10vh; top: 30%;}"
	6	(if (:preview @cozmo-db)
		".guide {left: 20%; top: 20%;}"
		".guide {left: 20%; top: 70%;}"
		)
))
(defn guide [] [:div {:class "guide bigEntrance three"
			:style {:border-color (:light @styl)
				:background (:secondary @styl)}}
		[:style (guide-style)]
		[:h2 {:style {:color (:light @styl)}}
		(case (+ @active 1)
		1 "Welcome to the Validator Portal!"
	2	(if (= "empty" (:profile @cozmo-db))
			"Please define your role."
			(case (:profile @cozmo-db)
"Hustler" "Nice to see you on board!"

"Hacker" "An expert at programming and solving problems"

"Designer" "So you are a creative guy."

"Sales" "Lorem ipsum dolor set.."

"Intern" "You've leveled up!"

"Lawyer" "Here comes the Lawyer!"

"Financial" "Cool, so you work in finance."

"Investor" "There's always a Bigger Fish.."

"Advisor" "Welcome here!"

"HR" "So HR is right for you?"

"Marketing" "Tell the world what you believe"

"Engineer" "Welcome on board!"


			)
			)
		3 "Select your skills"
		4 "Define goals"
		5 "Pick interests"
		6 "Create account"
		)]])

(defn card [num color name content][:li {:class (str "card-container " 
		(if (= @active num)"card-active""card-passive"))}
   [:div {:id "canvas"
	:class (str "card " name)
	:style (if (:toggle3D @cozmo-db)
	{:background "rgba(0,0,0,0)"}
	{:background "rgba(0,0,0,0)"}
	)}
	content]])

(defn menu [] 
  [:div.header {:style {:background (:secondary @styl)}}
[:div {:class "stepper slideDown"}
  [:div.stepper-steps [:ol
    (map (fn [z] [:li
	[:a {:on-click #(reset! active z)
	     :class (if (= @active z)"completed active""completed")
	     :style (if (= @active z){:background (:secondary @styl)}{})}
		z]])(range 6))][:span{:class "step-line"}]]]])

(defn steps [cards]
  [:section {:class "content"}
    [:div {  :style {:width (str(* 6 100)"%")}
	     :class (str "cards " "card" @active)} cards]])

;; Pages

(defn intro [] (muz [:div.home
  (menu)
  (steps
      [:ul
      	(card 0 false "" [:div (go-button)])
      	(card 1 false "simple" (tile-wrap))
      	(card 2 false "reflex" [:div [circular-nav] (go-button)])
      	(card 3 false "flat" [:div (tags) (go-button)])
      	(card 4 false "preview" [:div (gallery) (search-bar) (go-button)])
      	(card 5 true "end" [:div (text-block) (preview) (go-button)])
      ])
  (drawer)
  (orb)
  (guide)
  (progress-bar)
]))


(defn login-card []
 [:div {:class "login-card pullDown zero"}
		;[:div  {:class "background fade-into five"}]
  [:div {:class "overlay fade-in one"}
    [:a {:href "#/intro"}[:span {:class "tooltip"}]
		[:div {:class "fader four"}]
		[:span {:class "tooltip__content"}[:p "Registration"]]]
    [:p {:class "title fade-in two"}"Login"]
    [:div {:class "field username"}
      [:input {:type "text" :id "username" :required true}]
      [:label {:for "username"} "Username"]
    ]
    [:div {:class "field password"}
      [:input {:type "text" :id "password" :required true
		:style {:-webkit-text-security "square"}}]
      [:label {:for "password"} "Password"]
    ]
    [:button {:class "login-btn fade-in three"}"Go"]
    [:a {:class "forgot" :href "#"} "Forgot your Password?"]
  ]]
)

(defn home [ratom]
  [:div.landing
	[:div.top {:style {:display "none"}}
		[:div.brand [:h4 "Validator Portal"]]
		[:div.menu
		  [:button "Landing"]
		  [:button "About"]
		  [:button "Beta"]]]
	[login-card]
	[:div.footer "Validator Portal 0.1"]
])

(defn login [ratom]
  [:div [:h1 "About Page"]
   [:a {:href "#/"} "home page"]])

;; Initialize App

(defmulti page identity)
(defmethod page :home [] home)
(defmethod page :intro [] intro)
(defmethod page :login [] login)
(defmethod page :default [] (fn [_] [:div]))

(defn current-page [ratom]
  (let [page-key (:page @ratom)]
    [:div  [:style (str "body {background: radial-gradient(circle at bottom left, "
		(:primary @styl) ", " (:third @styl) ")}
		.fader:after {background-color:" (:secondary @styl) "}")]
    [(page page-key) ratom]]))

(defn reload []
  (reagent/render [current-page app-state]
                  (.getElementById js/document "app")))

(defn ^:export main []
  (dev-setup)
  (app-routes)
  (reload))
