# -*- coding: utf-8 -*-
from __future__ import unicode_literals

import json
import datetime

from django.db import connection
from django.http import HttpResponse
from django.shortcuts import render

from .models import Message

def index(request):
	if request.method == 'GET':
		messages = Message.objects.order_by('msg_date').all()
		response_data = []
		final_response = {}
		for m in messages:
			response_record = {}
			response_record['user'] = m.user
			response_record['date'] = m.msg_date.strftime("%Y-%m-%d_%H-%M-%S")
			response_record['text'] = m.chat_text
			response_data.append(response_record)
		final_response["log"] = response_data
		return HttpResponse(json.dumps(final_response, sort_keys=True), "application/json")
	else:
		return HttpResponse("")

def create(request):
	dateStr = request.GET['date']
	date = datetime.datetime.strptime(dateStr, "%Y-%m-%d_%H-%M-%S")
	m = Message(user=request.GET['user'], msg_date=date, chat_text=request.GET['text'])
	m.save()
	return HttpResponse("Success!")