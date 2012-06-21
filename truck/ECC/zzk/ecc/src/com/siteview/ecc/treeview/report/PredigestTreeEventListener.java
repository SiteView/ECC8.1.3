package com.siteview.ecc.treeview.report;

import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.siteview.ecc.treeview.controls.BaseTreeitem;

/**
 * �����¼���,��Ҫ���ڵ�����ڵ�ʱ,ѡȡ��ؽڵ�. 1.��չ��ʱ�жϵ�ǰ�ڵ�ѡ��״̬,���������ӽڵ�(����Ҷ�ڵ�)״̬; ����һ:
 * �����ӵ�ڵ���ڵ�������״̬��ͬ�ķ���
 * 
 * 2.��ڵ�A,������ͬ���ڵ���һ��ѡ��,���ڵ�ѡ��,״̬һֱӰ�쵽�����ڵ�, ������: ���и��ڵ�,��ڵ�ѡ�еķ���
 * 
 * 3.û��һ��ѡ��,���ڵ㲻ѡ��;��ȥN���ڵ�״̬��������ӽڵ�״̬��� ������: ���ڵ㲻ѡ��,������ڵ���б���,��һ����ѡ��,�����ڵ�ѡ��
 * 
 * 4.�����ӽڵ���A״̬��ͬ; ͬ����һ
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-3-24
 */
public class PredigestTreeEventListener implements EventListener {
	private final static Logger logger = Logger.getLogger(PredigestTreeEventListener.class);

	/**
	 * ����һ: �����ӵ�ڵ���ڵ�������״̬��ͬ�ķ���
	 * 
	 * @param t
	 */
	public void setChildrenSameSelf(BaseTreeitem t) {
		if (t != null) {
			/*
			 * logger.info(t.getLabel() + "**>>>" + "::::::" +
			 * t.isSelected()); Set set = t.getTree().getSelectedItems();
			 * Iterator it = set.iterator(); while (it.hasNext()) { Treeitem y =
			 * (Treeitem) it.next(); logger.info("---------" +
			 * y.getLabel()); }
			 */
			// 3.6.0�汾��ZKOSS t.isSelected()��Զ����true,���·���������ȷִ��
			// if (t.isSelected()) {// ��ǰ�ڵ㱻ѡ��,�����ӽڵ�,��ڵ�ҲӦѡ��
			if (t.isChecked()) {
				// if (t.getTree().getSelectedItem() == t) {
				// logger.info(t.getLabel() + " ��ѡ����");
				selectedAllChildren(t, true);
			} else {// ��ǰ�ڵ�û�б�ѡ��,�����ӽڵ�,��ڵ�ҲӦ��ѡ��
				unselectedAllChildren(t);
			}
			t = null;
		}

	}

	/**
	 * ������: ���и��ڵ�,��ڵ�ѡ�еķ���
	 * 
	 * @param r
	 */
	public void selectAllParent(Object r) {
		if (r != null) {
			Object p = null;
			if (r instanceof Treeitem) {
				((Treeitem) r).setSelected(true);
				setT(r);
				p = ((Treeitem) r).getParent();
				selectAllParent(p);
			} else if (r instanceof Treechildren) {
				p = ((Treechildren) r).getParent();
				selectAllParent(p);
			} else if (r instanceof Tree) {

			} else {
				log(r + " in selectAllParent������");
			}

		}
	}

	/**
	 * ������: ���ڵ㲻ѡ��,������ڵ���б���,��һ����ѡ��,�����ڵ�ѡ��
	 * 
	 * @param p
	 */
	public void selectParentAndLookForefathers(Treeitem p) {
		unselectedParent(p.getParent());
		lookForefathers(p.getParent());
	}

	/**
	 * 
	 * 
	 * @param t
	 */
	/**
	 * ѡ����������ڵ�
	 * 
	 * @param t
	 * @param need
	 *            needΪtrueʱѡ��,Ϊfalseʱ��ѡ��
	 */
	private void selectedAllChildren(Object t, boolean need) {
		List tcl = null;
		if (t instanceof Treechildren) {
			tcl = ((Treechildren) t).getChildren();
		} else if (t instanceof Treeitem) {
			tcl = ((Treeitem) t).getChildren();
		}
		if (tcl != null) {
			List lit = null;
			for (Object o : tcl) {
				if (o instanceof Treechildren) {
					lit = ((Treechildren) o).getChildren();
					for (Object x : lit) {
						if (x instanceof Treechildren) {
							selectedAllChildren(x, need);
						} else if (x instanceof Treeitem) {
							((Treeitem) x).setSelected(need);
							stf(x, need);
							selectedAllChildren((Treeitem) x, need);
						} else {
							log(x + " in  selectedAllChildren 2");
						}
					}

				} else if (o instanceof Treeitem) {
					((Treeitem) o).setSelected(need);
					stf(o, need);
					selectedAllChildren((Treeitem) o, need);
				} else if (o instanceof Treerow) {
					((Treeitem) ((Treerow) o).getParent()).setSelected(need);
					stf(((Treerow) o).getParent(), need);
				} else {
					log(o + " in  selectedAllChildren 1");
				}
			}
			tcl = null;
			lit = null;
		}
	}

	/**
	 * ������ڵ�,��һ����ѡ��,�����ڵ�ѡ��
	 * 
	 * @param t
	 */
	private void lookForefathers(Object t) {
		Object j = null;
		if (t != null) {
			if (t instanceof Treeitem) {
				if (jugdecurrent((Treeitem) t)) {// ��ǰ�ڵ㱻������ѡ����,���ټ�����������
					j = null;
				} else {
					j = ((Treeitem) t).getParent();
				}
			} else if (t instanceof Treechildren) {// ����������
				lookForefathers(((Treechildren) t).getParent());
			} else if (t instanceof Tree) {// ������
			} else {
				log(t + " in lookForefathers 3");
			}
		}

		if (j != null) {
			if (j instanceof Treeitem) {
				List<Object> litc = ((Treeitem) j).getChildren();
				// if (litc != null && litc.size() > 0) {
				for (Object o : litc) {
					if (o instanceof Treeitem) {
						// if (((Treeitem) o).isSelected()) {// ��һ���ڵ�ѡ��
						if (isc(o)) {
							((Treeitem) j).setSelected(true);// ����ڵ�ѡ��
							setT(j);
							break;// �˳�ѭ��
						}
					} else {
						log(o + " in lookForefathers 2");
					}
				}
				// }
				lookForefathers(j);
			} else if (j instanceof Treechildren) {
				lookForefathers(((Treechildren) j).getParent());
			} else {
				log(j + " in lookForefathers 1");
			}
		}
	}

	/**
	 * �жϵ�ǰ�ڵ��Ƿ�Ӧ��ѡ��,��������һ���ӽڵ�,����һ���ӽڵ㱻ѡ��,��ѡ�е�ǰ�ڵ�,����֮
	 * 
	 * @param m
	 */
	private boolean jugdecurrent(Treeitem m) {
		if (m == null)
			return false;
		m.setSelected(false);// Ĭ��Ϊ��ѡ��
		setF(m);
		List<Object> litc = m.getChildren();
		for (Object o : litc) {// �����ӽڵ�
			if (o instanceof Treeitem) {
				// if (((Treeitem) o).isSelected()) {// ��һ���ڵ�ѡ��
				if (isc(o)) {
					m.setSelected(true);// ����ڵ�ѡ��
					setT(m);
					return true;
				}
			} else if (o instanceof Treechildren) {
				List tb = ((Treechildren) o).getChildren();
				for (Object e : tb) {
					if (e instanceof Treeitem) {
						// if (((Treeitem) e).isSelected()) {
						if (isc(e)) {
							m.setSelected(true);// ����ڵ�ѡ��
							setT(m);
							return true;
						}
					} else {
						log(e + " in jugdecurrent");
					}
				}

			} else if (o instanceof Treerow) {
				// if (((Treeitem) ((Treerow) o).getParent()).isSelected()) {
				if (isc(((Treerow) o).getParent())) {
					m.setSelected(true);// ����ڵ�ѡ��
					setT(m);
					return true;
				}
			} else {
				log(o + " in lookForefathers 4");
			}
		}
		litc = null;
		return false;
	}

	/**
	 * ��ѡ����������ڵ�
	 * 
	 * @param t
	 */
	private void unselectedAllChildren(Treeitem t) {
		selectedAllChildren(t, false);
	}

	/**
	 * ��ѡ�и��ڵ�
	 * 
	 * @param t
	 */
	private void unselectedParent(Object t) {
		if (t != null) {
			if (t instanceof Treeitem) {
				((Treeitem) t).setSelected(false);
				setF(t);
			} else if (t instanceof Treechildren) {
				unselectedParent(((Treechildren) t).getParent());
			} else if (t instanceof Tree) {

			} else {
				log(t + " in unselectedParent");
			}

		}
	}

	public void onEvent(Event event) {
		Treerow tr = (Treerow) event.getTarget();
		BaseTreeitem tri = (BaseTreeitem) tr.getParent();
		// tri.getTree().
		// logger.info("��ǰ�ڵ�:" + tri.getLabel() + "--" + tri.is);
		/*
		 * try { Messagebox.show(tri.getLabel()+tri.isSelected()); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		List li = null;
		if (tri != null) {
			if (tri.isChecked()) {
				tri.setChecked(false);
			} else {
				tri.setChecked(true);
			}
			if (tri.getParent() != null
					&& tri.getParent() instanceof Treechildren) {
				li = tri.getParent().getChildren();// ����ͬ���ڵ�
			} else {
				li = tri.getChildren();// ����ͬ���ڵ�
			}
		}

		boolean isselect = false;// �ж���û��ͬ���ڵ㱻ѡ��,ѡ��Ϊtrue
		if (li != null) {
			Object o = null;

			for (int a = 0; a < li.size(); a++) {// ����ͬ���ڵ�(�ֵܽڵ�)
				o = li.get(a);
				if (o instanceof Treerow) {// oΪ Ҷ�ڵ�
					Treeitem t = (Treeitem) (((Treerow) o).getParent());
					// if (t.isSelected()) {// ͬ���ڵ㱻ѡ��
					if (isc(t)) {
						selectAllParent(t.getParent());// ���и�,��ڵ㱻ѡ��;��������
						isselect = true;
						break;// ���ٱ�����
					}
				} else if (o instanceof Treeitem) {// oΪ��Ҷ�ڵ�
					// if (((Treeitem) o).isSelected()) {// ͬ���ڵ㱻ѡ��
					if (isc(o)) {
						selectAllParent(((Treeitem) o).getParent());//���и�,��ڵ㱻ѡ��
						// ;��������
						isselect = true;
						break;// ���ٱ�����
					}
				} else if (o instanceof Treechildren) {
					// ����ͬһ���ڵ���,������
				} else {
					log(o + " in  onEvent ������");
				}
			}
			o = null;
		}
		if (!isselect) {// û��ͬ���ڵ�ѡ��,��������
			selectParentAndLookForefathers(tri);
		}
		setChildrenSameSelf(tri);// ��������ڵ�������״̬��ͬ
	}

	private boolean isc(Object o) {
		if (o != null) {
			return (((BaseTreeitem) o).isChecked());
		}
		return false;
	}

	private void setT(Object s) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(true);
		}
	}

	private void setF(Object s) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(false);
		}
	}

	private void stf(Object s, boolean b) {
		if (s != null) {
			((BaseTreeitem) s).setChecked(b);
		}
	}

	private void log(Object o) {
		logger.info("�����ش�BUG,֪ͨdi.tang����:" + o);
	}
}
