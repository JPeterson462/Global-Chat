# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models

class Message(models.Model):
	chat_text = models.CharField(max_length=256)
	msg_date = models.DateTimeField('date sent')
	user = models.CharField(max_length=32)