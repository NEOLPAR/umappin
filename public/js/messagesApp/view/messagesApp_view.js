var messagesApp = messagesApp || {};
messagesApp.DISCUSSION_LIST_BODY ='<ul id="discussion_list"></ul>';

//this is the view controller for the all messages APP
(function(){
	messagesApp.AppView = Backbone.View.extend({
		el: 'body',
		initialize: function () {
			
			//if the add method of Discussions is called  "this.addOne" whill be triggered
			this.listenTo(messagesApp.DiscussionHeaders, 'add', this.addOneDiscussionHeader);
			this.listenTo(messagesApp.DiscussionHeaders, 'reset', this.restartDiscussionHeaders);

			//messagesApp.Discussions.fetch();
		},
		//Discussion Headers methods
		addOneDiscussionHeader: function (discussionHeader) {
			//Create ReceivedView and append it to the list
			console.log("add one message");
			var view = new messagesApp.ReceivedView({ model: discussionHeader });

			$('#discussion_list').append(view.render().el);
		},
		restartDiscussionHeaders: function(){
			console.log('Changed to received discussions');
			$('#messages_body').html(messagesApp.DISCUSSION_LIST_BODY);
		}

		//End Discussions Headers methods
	});
})();

//created here ... it will be at another place,...
var appView = new messagesApp.AppView();
